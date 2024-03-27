package com.patinaud.bataillepersistence.mapper;

import com.patinaud.bataillemodel.dto.GameDTO;
import com.patinaud.bataillepersistence.entity.Game;

public class GameMapper {

    public static Game toEntity(GameDTO gameDto) {
        Game game = new Game();
        game.setIdGame(gameDto.getIdGame());
        game.setIdPlayerTurn(gameDto.getIdPlayerTurn());
        return game;
    }
}
