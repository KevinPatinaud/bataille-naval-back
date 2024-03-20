package com.patinaud.batailleapi.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.patinaud.batailleapi.mapper.BoatMapper;
import com.patinaud.batailleapi.requestdata.Boat;
import com.patinaud.batailleapi.requestdata.Coordinate;
import com.patinaud.batailleengine.gameengine.GameEngineService;
import com.patinaud.bataillemodel.dto.BoatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GameWebSocketController {

    @Autowired
    GameEngineService gameEngineService;

    private Gson gson = new Gson();

    @MessageMapping("/{idGame}/attack/{idPlayer}")
    public void processAttackFromPlayer(@DestinationVariable("idGame") String idGame, @DestinationVariable("idPlayer") String idPlayer, @Payload Coordinate coordinate) throws Exception {

        gameEngineService.playerAttack(idGame, idPlayer, coordinate.getX(), coordinate.getY());

    }


    @MessageMapping("/{idGame}/submit-boat/{idPlayer}")
    public void submitBoat(@DestinationVariable("idGame") String idGame, @DestinationVariable("idPlayer") String idPlayer, @Payload String boats_json) throws Exception {

        List<Boat> boats = gson.fromJson(boats_json, new TypeToken<List<Boat>>() {
        }.getType());
        gameEngineService.positionHumanPlayerBoat(idGame, BoatMapper.toDtos(boats));
    }


    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}