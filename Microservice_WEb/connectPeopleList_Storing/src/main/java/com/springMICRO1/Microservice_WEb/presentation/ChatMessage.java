package com.springMICRO1.Microservice_WEb.presentation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Add an ID for the message

    private String sender;           // The sender of the message
    private String receiverContact;  // The receiver's contact info
    private String message;          // The message content

    @jakarta.persistence.Transient
    private String image;            // Transient field for the image (not saved)

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image; // Get the transient image field
    }

    public void setImage(String image) {
        this.image = image; // Set the transient image field
    }

    @Override
    public String toString() {
        return "ChatMessage [sender=" + sender + 
               ", receiverContact=" + receiverContact + 
               ", message=" + message + 
               ", image=" + (image != null ? "[image]" : "null") + "]";
    }
}
