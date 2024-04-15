package com.patinaud.bataillepersistence.mapper;

import com.patinaud.bataillemodel.dto.GameDTO;
import com.patinaud.bataillepersistence.entity.Game;

public class GameMapper {
    private GameMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Game toEntity(GameDTO dto) {
        Game entity = new Game();
        entity.setId(dto.getId());
        entity.setIdPlayerTurn(dto.getIdPlayerTurn());
        entity.setMode(dto.getMode());
        return entity;
    }

    public static GameDTO toDto(Game entity) {
        GameDTO dto = new GameDTO();
        dto.setId(entity.getId());
        dto.setMode(entity.getMode());
        dto.setIdPlayerTurn(entity.getIdPlayerTurn());

        return dto;
    }
}
