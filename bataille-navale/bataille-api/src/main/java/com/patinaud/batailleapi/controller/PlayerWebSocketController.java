package com.patinaud.batailleapi.controller;

import com.patinaud.batailleservice.service.gameengine.GameEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerWebSocketController {

    @Autowired
    GameEngineService gameEngineService;

    @MessageMapping("/{idGame}/attack/{idPlayer}")
    public void processAttackFromPlayer(@DestinationVariable("idGame") String idGame, @DestinationVariable("idPlayer") String idPlayer, @Payload String message) throws Exception {
        System.out.println(idGame);
        System.out.println(message);


        gameEngineService.playerAttack(idGame, idPlayer, 0, 0);

        // messagingTemplate peut être appelé à travers une autre méthode, ce qui permet d'envoyer une réponse dans un second temps
        // indiquer l'id unique de la partie dans l'URL de la réponse
        //    this.messagingTemplate.convertAndSend("/diffuse/" + idGame + "/attack", "{ \"name\" : \"Coucou\"}");
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        //     messagingTemplate.convertAndSend("/errors", exception.getMessage());
        return exception.getMessage();
    }
}