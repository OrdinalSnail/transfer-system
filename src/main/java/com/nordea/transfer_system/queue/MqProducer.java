package com.nordea.transfer_system.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MqProducer {

    private static final String URL = "tcp://localhost:61616";

    public void send(String queueName, String msg) {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage(msg);
            producer.send(message);
            System.out.println("Sent message '" + message.getText() + "'");
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
