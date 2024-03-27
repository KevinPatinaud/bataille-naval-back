package com.patinaud.bataillepersistence.entity;

import com.patinaud.bataillemodel.dto.PlayerDTO;
import com.patinaud.bataillepersistence.mapper.GameMapper;

public class PlayerMapper {
    public static Player toEntity(PlayerDTO playerDto) {
        Player player = new Player();
        player.setGame(GameMapper.toEntity(playerDto.getGame()));
        player.setIdPlayer(playerDto.getIdPlayer());
        player.setIA(playerDto.isIA());
        return player;
    }
}
