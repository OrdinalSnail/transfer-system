package com.nordea.transfer_system;

import com.nordea.transfer_system.generated.xsd.OutcomeType;
import com.nordea.transfer_system.generated.xsd.TransferRequestType;
import com.nordea.transfer_system.generated.xsd.TransferResponseType;
import com.nordea.transfer_system.message.MessageConverter;
import com.nordea.transfer_system.message.MessageValidator;
import com.nordea.transfer_system.queue.MqConsumer;
import com.nordea.transfer_system.queue.MqProducer;
import com.nordea.transfer_system.processing.AccountProcessor;
import com.nordea.transfer_system.processing.ConsoleParser;
import com.nordea.transfer_system.processing.UserAccountProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

import static com.nordea.transfer_system.consts.ConsoleArg.*;

@SpringBootApplication
public class TransferSystemApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TransferSystemApplication.class);

    @Autowired
    UserAccountProcessor userAccountProcessor;
    @Autowired
    AccountProcessor accountProcessor;


    public static void main(String[] args) {
        SpringApplication.run(TransferSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        System.out.println("Let's start");

        ConsoleParser cp = new ConsoleParser();
        cp.parseParameters(args);

        userAccountProcessor.updateInfo(cp.getValue(CA_CONFIG_PATH));

        MessageValidator validator = new MessageValidator();
        MessageConverter converter = new MessageConverter();

        MqConsumer consumer = new MqConsumer();
        MqProducer producer = new MqProducer();

        System.out.println("consume queue: " + cp.getValue(CA_INC_QUEUE));

        while (true) {
            System.out.println(".");
            String msg = consumer.consume(cp.getValue(CA_INC_QUEUE));
            //if xml is invalid -- only save log without response
            if (msg != null && validator.isValid("xsd/transfer-request-response.xsd", msg)) {
                TransferRequestType request = converter.makeRequest(msg);
                OutcomeType action = accountProcessor.processingRequest(request);
                TransferResponseType response = converter.convertToResponse(request, action);
                String responseXML = converter.makeXMlResponse(response);
                producer.send(cp.getValue(CA_OUT_QUEUE), responseXML);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                LOG.error(null, ex);
            }
        }
    }

}
