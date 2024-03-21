package com.patinaud.batailleapi.mapper;

import com.patinaud.batailleapi.requestdata.Game;
import com.patinaud.bataillemodel.dto.GameDTO;

public class GameMapper {
    private GameMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Game toResponse(GameDTO gameDTO) {
        Game game = new Game();
        game.setIdGame(gameDTO.getIdGame());
        return game;
    }
}
