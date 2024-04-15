package com.patinaud.batailleapi.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.patinaud.batailleapi.mapper.BoatMapper;
import com.patinaud.batailleapi.mapper.CoordinateMapper;
import com.patinaud.batailleapi.response.Boat;
import com.patinaud.batailleapi.response.Coordinate;
import com.patinaud.batailleengine.gameengine.GameEngineService;
import com.patinaud.bataillemodel.constants.IdPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GameWebSocketController {

    private final Gson gson = new Gson();
    GameEngineService gameEngineService;

    @Autowired
    public GameWebSocketController(GameEngineService gameEngineService) {
        this.gameEngineService = gameEngineService;
    }


    @MessageMapping("/{idGame}/attack/{idPlayer}")
    public void processAttackFromPlayer(@DestinationVariable("idGame") String idGame, @DestinationVariable("idPlayer") String idPlayer, @Payload Coordinate coordinate) {
        gameEngineService.playerAttack(idGame, IdPlayer.valueOf(idPlayer.toUpperCase()), CoordinateMapper.toDto(coordinate));
    }


    @MessageMapping("/{idGame}/submit-boat/{idPlayer}")
    public void submitBoat(@DestinationVariable("idGame") String idGame, @DestinationVariable("idPlayer") String idPlayer, @Payload String boatsJson) {

        List<Boat> boats = gson.fromJson(boatsJson, new TypeToken<List<Boat>>() {
        }.getType());
        gameEngineService.positionPlayerBoats(idGame, IdPlayer.valueOf(idPlayer.toUpperCase()), BoatMapper.toDtos(boats));
    }


    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}