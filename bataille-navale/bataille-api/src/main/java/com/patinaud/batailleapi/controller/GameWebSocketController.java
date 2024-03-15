package com.patinaud.batailleapi.controller;

import com.google.gson.Gson;
import com.patinaud.batailleapi.requestobj.Coordinate;
import com.patinaud.batailleservice.service.gameengine.GameEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
public class GameWebSocketController {

    @Autowired
    GameEngineService gameEngineService;


    @MessageMapping("/{idGame}/attack/{idPlayer}")
    public void processAttackFromPlayer(@DestinationVariable("idGame") String idGame, @DestinationVariable("idPlayer") String idPlayer, @Payload String coordinate_json) throws Exception {

        Gson gson = new Gson();
        Coordinate coord = gson.fromJson(coordinate_json, Coordinate.class);

        gameEngineService.playerAttack(idGame, idPlayer, coord.getX(), coord.getY());

    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        //     messagingTemplate.convertAndSend("/errors", exception.getMessage());
        return exception.getMessage();
    }
}