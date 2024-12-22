package com.springMICRO1.Microservice_WEb.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springMICRO1.Microservice_WEb.presentation.ConnectedPeoples;
import com.springMICRO1.Microservice_WEb.presentation.Message;
import com.springMICRO1.Microservice_WEb.repositiory.MessageRepository;
import com.springMICRO1.Microservice_WEb.repositiory.PersonRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PersonRepository connectedPeoplesRepository;

    public Message saveMessage(String senderContact, String receiverContact, String messageContent, byte[] imageBytes) {
        List<ConnectedPeoples> connectedPeoplesList = connectedPeoplesRepository
                .findByContact(receiverContact);

        if (connectedPeoplesList.isEmpty()) {
            throw new RuntimeException("No connected people found with the provided contact.");
        }

        ConnectedPeoples connectedPeoples = connectedPeoplesList.get(0);

        // Create and set the message with necessary fields
        Message message = new Message();
        message.setSenderContact(senderContact);
        message.setReceiverContact(receiverContact);
        message.setMessageContent(messageContent);
        message.setImage(imageBytes);
        message.setConnectedPeoples(connectedPeoples); // Set the connected people

        return messageRepository.save(message); // Save the message
    }

    public List<ConnectedPeoples> getConnectedPeoplesList(String contact) {
        // Fetch all messages where the given contact is either sender or receiver
        List<Message> messages = messageRepository.findBySenderContactOrReceiverContact(contact, contact);

        // Extract all other contacts (sender/receiver excluding the given contact)
        Set<String> otherContacts = messages.stream()
                .map(message -> contact.equals(message.getSenderContact())
                        ? message.getReceiverContact()
                        : message.getSenderContact())
                .collect(Collectors.toSet());

        // Fetch all ConnectedPeoples in one query
        Map<String, String> contactNameMap = connectedPeoplesRepository.findByContactIn(otherContacts).stream()
                .collect(Collectors.toMap(ConnectedPeoples::getContact, ConnectedPeoples::getName));

        // Map messages to ConnectedPeoples
        return messages.stream().map(message -> {
            String otherContact = contact.equals(message.getSenderContact())
                    ? message.getReceiverContact()
                    : message.getSenderContact();

            ConnectedPeoples connected = new ConnectedPeoples();
            connected.setContact(otherContact);
            connected.setName(contactNameMap.getOrDefault(otherContact, "Unknown"));
            connected.setMessage(message.getMessageContent());
            return connected;
        }).collect(Collectors.toList());
    }

    public List<Message> getMessagesBySenderAndReceiver(String senderContact, String receiverContact) {
        return messageRepository.findBySenderContactAndReceiverContact(senderContact, receiverContact);
    }

}
