package com.springMICRO1.Microservice_WEb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

@Configuration
@EnableWebSocketMessageBroker // This annotation enables WebSocket message handling, backed by a message
                              // broker.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket") // WebSocket endpoint
                .setAllowedOrigins("http://localhost:3000") // Allow CORS from the frontend
                .withSockJS(); // Fallback for older browsers
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // Client-to-server prefix
        registry.enableSimpleBroker("/topic", "/queue"); // Server-to-client message prefixes
        registry.setUserDestinationPrefix("/user"); // Private messaging prefix
    }

    @Override
    public void configureWebSocketTransport(
            org.springframework.web.socket.config.annotation.WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(5 * 1024 * 1024); // Set to 5 MB (5 * 1024 * 1024 bytes)
        registry.setSendBufferSizeLimit(10 * 1024 * 1024); // Set to 10 MB (5 MB for message size + buffer size)
        registry.setSendTimeLimit(20_000); // Increase the send timeout (in ms) if needed
    }

}