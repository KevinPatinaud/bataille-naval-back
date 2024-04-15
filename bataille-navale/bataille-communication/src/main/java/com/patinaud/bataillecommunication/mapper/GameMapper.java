package com.patinaud.bataillecommunication.mapper;

import com.patinaud.bataillecommunication.response.Game;
import com.patinaud.bataillecommunication.response.PlayerState;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.GameDTO;
import com.patinaud.bataillemodel.dto.GridDTO;

import java.util.List;
import java.util.stream.Collectors;

public class GameMapper {

    private GameMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Game toResponse(GameDTO gameDto, GridDTO gridPlayer1Dto, List<BoatDTO> boatsPlayer1Dto, GridDTO gridPlayer2Dto, List<BoatDTO> boatsPlayer2Dto) {
        Game response = new Game();

        response.setId(gameDto.getId());
        response.setIdPlayerTurn(gameDto.getIdPlayerTurn());
        response.setMode(gameDto.getMode());

        PlayerState responsePlayer1 = new PlayerState();
        responsePlayer1.setCells(CellsMapper.fromDtosToResponses(gridPlayer1Dto.getCells().stream().flatMap(List::stream).collect(Collectors.toList())));
        responsePlayer1.setBoatsStates(BoatsMapper.fromDtoToResponse(boatsPlayer1Dto));
        response.setPlayer1(responsePlayer1);


        PlayerState responsePlayer2 = new PlayerState();
        responsePlayer2.setCells(CellsMapper.fromDtosToResponses(gridPlayer2Dto.getCells().stream().flatMap(List::stream).collect(Collectors.toList())));
        responsePlayer2.setBoatsStates(BoatsMapper.fromDtoToResponse(boatsPlayer2Dto));
        response.setPlayer2(responsePlayer2);

        return response;
    }
}
