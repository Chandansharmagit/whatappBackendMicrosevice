package com.springMICRO1.Microservice_WEb.repositiory;

import com.springMICRO1.Microservice_WEb.presentation.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderContactOrReceiverContact(String senderContact, String receiverContact);

    List<Message> findBySenderContactAndReceiverContact(String senderContact, String receiverContact);
}

