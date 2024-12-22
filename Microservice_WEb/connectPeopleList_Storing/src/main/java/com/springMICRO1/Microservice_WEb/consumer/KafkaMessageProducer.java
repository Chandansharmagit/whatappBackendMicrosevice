package com.springMICRO1.Microservice_WEb.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.springMICRO1.Microservice_WEb.presentation.ChatMessage;

import java.util.concurrent.ExecutionException;

@Service
public class KafkaMessageProducer {

    private static final String TOPIC = "chat-topic";

    @Autowired
    private KafkaTemplate<String, ChatMessage> kafkaTemplate;

    /**
     * Sends a chat message to the Kafka topic.
     *
     * @param chatMessage the message to send
     */
    public void sendMessage(ChatMessage chatMessage) {
        try {
            kafkaTemplate.send(TOPIC, chatMessage).get(); // Ensures synchronous send for error checking
            System.out.println("Message sent to Kafka topic: " + chatMessage);
        } catch (ExecutionException e) {
            System.err.println("Error sending message to Kafka: " + e.getCause().getMessage());
        } catch (InterruptedException e) {
            System.err.println("Message sending was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupted state
        } catch (Exception e) {
            System.err.println("Unexpected error sending message to Kafka: " + e.getMessage());
        }
    }
    
}
