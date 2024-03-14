package com.patinaud.batailleservice.service.communication;

public interface CommunicationService {

    public void sendMessage(String idGame, String action, String data);
}
