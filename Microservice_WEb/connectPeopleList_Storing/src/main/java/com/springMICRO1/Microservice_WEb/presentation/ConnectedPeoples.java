package com.springMICRO1.Microservice_WEb.presentation;

import jakarta.persistence.*;

@Entity
public class ConnectedPeoples {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true) // Allows null values
    private String name;

    @Column(nullable = true) // Allows duplicates
    private String contact;

    @Column(nullable = true) // Allows duplicates
    private String email;

    @Column(columnDefinition = "LONGBLOB", nullable = true) // For storing image as a blob
    private String image;

    @Column(nullable = true) // Not required
    private String message; // Optional field for additional message details

    @ManyToOne
    @JoinColumn(name = "message_list", nullable = true) // Foreign key to Message entity
    private Message message2; // Reference to the Message entity

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message getMessage2() {
        return message2;
    }

    public void setMessage2(Message message2) {
        this.message2 = message2;
    }

 
    
}
