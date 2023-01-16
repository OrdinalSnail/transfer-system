package com.nordea.transfer_system.queue;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class MqConsumer {

    private static final String URL = "tcp://localhost:61616";

    private boolean doLoop = true;

    public String consume(String queueName) {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(URL);
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(queue);
            Message message = consumer.receive(1000);
            session.close();
            connection.close();
            if (message instanceof TextMessage textMessage) {
                String text = textMessage.getText();
                System.out.println("Consumer Received: " + text);
                return text;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
