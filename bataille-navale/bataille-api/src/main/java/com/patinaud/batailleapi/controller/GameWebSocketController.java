package com.patinaud.batailleapi.controller;

import com.google.gson.Gson;
import com.patinaud.batailleapi.requestdata.Coordinate;
import com.patinaud.batailleengine.gameengine.GameEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
public class GameWebSocketController {

    @Autowired
    GameEngineService gameEngineService;

    private Gson gson = new Gson();

    @MessageMapping("/{idGame}/attack/{idPlayer}")
    public void processAttackFromPlayer(@DestinationVariable("idGame") String idGame, @DestinationVariable("idPlayer") String idPlayer, @Payload String coordinate_json) throws Exception {

        Coordinate coord = gson.fromJson(coordinate_json, Coordinate.class);
        gameEngineService.playerAttack(idGame, idPlayer, coord.getX(), coord.getY());

    }


    @MessageMapping("/{idGame}/submit-boat/{idPlayer}")
    public void submitBoat(@DestinationVariable("idGame") String idGame, @DestinationVariable("idPlayer") String idPlayer, @Payload String boats_json) throws Exception {
        // System.out.println(boats_json);

        //    Coordinate coord = gson.fromJson(coordinate_json, Coordinate.class);
        //   gameEngineService.playerAttack(idGame, idPlayer, coord.getX(), coord.getY());

    }


    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        //     messagingTemplate.convertAndSend("/errors", exception.getMessage());
        return exception.getMessage();
    }
}