package com.springMICRO1.Microservice_WEb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springMICRO1.Microservice_WEb.presentation.ChatMessage;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ChatMessageDeserializer implements Deserializer<ChatMessage> {

    private ObjectMapper objectMapper;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public ChatMessage deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, ChatMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {
        // No-op, no resources to close
    }
}
