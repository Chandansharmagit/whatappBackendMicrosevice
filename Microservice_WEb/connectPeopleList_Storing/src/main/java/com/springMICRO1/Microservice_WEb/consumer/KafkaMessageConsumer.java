package com.springMICRO1.Microservice_WEb.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.springMICRO1.Microservice_WEb.presentation.ChatMessage;
import com.springMICRO1.Microservice_WEb.presentation.Message;
import com.springMICRO1.Microservice_WEb.service.PersonService;

@Service
public class KafkaMessageConsumer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private PersonService personService; // Injecting PersonService to check if the user exists

  

    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void consumeMessage(ChatMessage chatMessage) {
        try {
            System.out.println("Message received from Kafka topic: " + chatMessage);
    
            // Check if the receiver exists in the database
            boolean userExists = personService.checkIfUserExists(chatMessage.getReceiverContact());
    
            if (userExists) {
                System.out.println("User exists, proceeding to save message.");
        
    
                // Send message to WebSocket topic for the receiver
                messagingTemplate.convertAndSend(
                    "/queue/messages/" + chatMessage.getSender(),
                    chatMessage
                );
    
                System.out.println("Message sent to receiver: " + chatMessage.getReceiverContact());
            } else {
                System.out.println("Receiver not found in the database: " + chatMessage.getReceiverContact());
            }
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }
    
}
