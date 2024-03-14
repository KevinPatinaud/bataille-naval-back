package com.patinaud.batailleservice.service.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;


@Service
public class CommunicationService_impl implements CommunicationService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Override
    public void sendMessage(String idGame, String action, String data) {
        System.out.println("/diffuse/" + idGame + "/" + action);
        this.messagingTemplate.convertAndSend("/diffuse/" + idGame + "/" + action, data);
    }
}
