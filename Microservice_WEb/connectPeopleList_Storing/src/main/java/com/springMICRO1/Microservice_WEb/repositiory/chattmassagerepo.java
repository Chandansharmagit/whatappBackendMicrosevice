package com.springMICRO1.Microservice_WEb.repositiory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springMICRO1.Microservice_WEb.presentation.ChatMessage;

@Repository
public interface chattmassagerepo extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findByReceiverContact(String receiverContact);
}
