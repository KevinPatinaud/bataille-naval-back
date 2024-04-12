package com.patinaud.bataillepersistence.mapper;

import com.patinaud.bataillemodel.dto.PlayerDTO;
import com.patinaud.bataillepersistence.entity.Player;

public class PlayerMapper {
    private PlayerMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Player toEntity(PlayerDTO playerDto) {
        Player player = new Player();
        player.setGame(GameMapper.toEntity(playerDto.getGame()));
        player.setIdPlayer(playerDto.getIdPlayer());
        player.setIA(playerDto.isIA());
        return player;
    }
}
