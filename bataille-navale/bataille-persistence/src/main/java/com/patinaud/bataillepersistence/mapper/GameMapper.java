package com.patinaud.bataillepersistence.mapper;

import com.patinaud.bataillemodel.dto.GameDTO;
import com.patinaud.bataillepersistence.entity.Game;

public class GameMapper {
    private GameMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Game toEntity(GameDTO gameDto) {
        Game game = new Game();
        game.setId(gameDto.getId());
        game.setIdPlayerTurn(gameDto.getIdPlayerTurn());
        return game;
    }
}
