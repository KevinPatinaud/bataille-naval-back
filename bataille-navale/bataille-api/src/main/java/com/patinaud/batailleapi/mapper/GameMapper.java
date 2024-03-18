package com.patinaud.batailleapi.mapper;

import com.patinaud.batailleapi.requestdata.Game;
import com.patinaud.bataillemodel.dto.GameDTO;

public class GameMapper {
    public static Game toResponse(GameDTO gameDTO) {
        Game game = new Game();
        game.setIdGame(gameDTO.getIdGame());
        return game;
    }
}
