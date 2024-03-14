package com.patinaud.batailleapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class PlayerWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/batailleNavale")
                .setAllowedOrigins("*");
        // .withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/diffuse"); // liste des URL de sorties :  peut prendre une liste de String   ... , "/prefixe_2/");
        config.setApplicationDestinationPrefixes("/action"); // liste des urls d'entrée : peut prendre une liste de String   ... , "/prefixe_2/");
    }


}