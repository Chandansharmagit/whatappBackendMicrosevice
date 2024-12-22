package com.springMICRO1.Microservice_WEb.presentation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderContact;
    private String receiverContact;
    private String messageContent;

    @Lob
    @Column(nullable = true, length = Integer.MAX_VALUE)
    private byte[] image; // Store image data as byte array using LONGBLOB

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "connected_peoples_id")
    private ConnectedPeoples connectedPeoples;  // Link each message to a person

    // Constructors
    public Message() {
        this.timestamp = LocalDateTime.now(); // Default timestamp
    }

    public Message(String senderContact, String receiverContact, String messageContent, byte[] image, ConnectedPeoples connectedPeoples) {
        this.senderContact = senderContact;
        this.receiverContact = receiverContact;
        this.messageContent = messageContent;
        this.image = image != null ? image : new byte[0]; // Handle null safely
        this.timestamp = LocalDateTime.now(); // Set timestamp to current time
        this.connectedPeoples = connectedPeoples; // Link connected peoples to message
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderContact() {
        return senderContact;
    }

    public void setSenderContact(String senderContact) {
        this.senderContact = senderContact;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image != null ? image : new byte[0]; // Handle null safely
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ConnectedPeoples getConnectedPeoples() {
        return connectedPeoples;
    }

    public void setConnectedPeoples(ConnectedPeoples connectedPeoples) {
        this.connectedPeoples = connectedPeoples;
    }
}
