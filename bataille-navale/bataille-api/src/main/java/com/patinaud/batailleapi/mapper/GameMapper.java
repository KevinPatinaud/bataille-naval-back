package com.patinaud.batailleapi.mapper;

import com.patinaud.batailleapi.response.Game;
import com.patinaud.bataillemodel.dto.GameDTO;

public class GameMapper {
    private GameMapper() {
        throw new IllegalStateException("Utility class");
    }


    public static Game toResponse(GameDTO gameDTO) {
        Game game = new Game();
        game.setId(gameDTO.getId());
        return game;
    }
}
