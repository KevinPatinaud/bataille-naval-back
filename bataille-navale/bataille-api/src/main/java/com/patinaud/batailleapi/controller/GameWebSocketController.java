package com.patinaud.batailleapi.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.patinaud.batailleapi.mapper.BoatMapper;
import com.patinaud.batailleapi.mapper.CoordinateMapper;
import com.patinaud.batailleapi.requestdata.Boat;
import com.patinaud.batailleapi.requestdata.Coordinate;
import com.patinaud.batailleengine.gameengine.GameEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GameWebSocketController {

    GameEngineService gameEngineService;

    private Gson gson = new Gson();

    @Autowired
    public GameWebSocketController(GameEngineService gameEngineService) {
        this.gameEngineService = gameEngineService;
    }

    @MessageMapping("/{idGame}/attack/{idPlayer}")
    public void processAttackFromPlayer(@DestinationVariable("idGame") String idGame, @DestinationVariable("idPlayer") String idPlayer, @Payload Coordinate coordinate) {

        gameEngineService.playerAttack(idGame, idPlayer, CoordinateMapper.toDto(coordinate));

    }


    @MessageMapping("/{idGame}/submit-boat/{idPlayer}")
    public void submitBoat(@DestinationVariable("idGame") String idGame, @DestinationVariable("idPlayer") String idPlayer, @Payload String boatsJson) {

        List<Boat> boats = gson.fromJson(boatsJson, new TypeToken<List<Boat>>() {
        }.getType());
        gameEngineService.positionHumanPlayerBoat(idGame, BoatMapper.toDtos(boats));
    }


    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}