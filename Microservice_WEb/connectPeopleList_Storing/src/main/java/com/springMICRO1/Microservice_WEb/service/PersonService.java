// src/main/java/com/example/demo/service/PersonService.java
package com.springMICRO1.Microservice_WEb.service;

import com.springMICRO1.Microservice_WEb.presentation.ConnectedPeoples;
import com.springMICRO1.Microservice_WEb.repositiory.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository connectedPeoplesRepository;
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public ConnectedPeoples addPerson(ConnectedPeoples newPerson) {

        return personRepository.save(newPerson);
    }

    public List<ConnectedPeoples> getConnectedPeoplesByEmail(String email) {
        // Fetch connected people by email
        return personRepository.findByEmail(email); // Repository query for email
    }

    public boolean checkIfUserExists(String contact) {
        // Implement your logic to check if a user with the given contact exists
        return personRepository.existsByContact(contact);
    }

    public List<ConnectedPeoples> getConnectedPeopleByContact(String contact) {
        return personRepository.findByContact(contact);
    }

    public void addConnectedContact(String receiverContact) {
        // Find existing connected people by the receiver's contact
        List<ConnectedPeoples> connectedPeople = connectedPeoplesRepository.findByContact(receiverContact);
    
        if (connectedPeople.isEmpty()) {
            // If no existing connection, create a new one
            ConnectedPeoples newConnected = new ConnectedPeoples();
            newConnected.setContact(receiverContact);
            connectedPeoplesRepository.save(newConnected);
        } else {
            // If connection already exists, update the existing contact
            ConnectedPeoples existingConnection = connectedPeople.get(0);
            existingConnection.setContact(receiverContact); // Update with new contact
            connectedPeoplesRepository.save(existingConnection);
        }
    }
    

}
