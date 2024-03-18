package com.patinaud.bataillepersistence.mapper;

import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillepersistence.entity.Boat;
import com.patinaud.bataillepersistence.entity.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BoatMapper {
    public static ArrayList<Boat> toEntities(ArrayList<BoatDTO> boatsDtos, Player player) {
        return boatsDtos.stream().map(c -> toEntity(c, player)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Boat toEntity(BoatDTO boatDto, Player player) {
        Boat boat = new Boat();
        boat.setPlayer(player);
        boat.setBoatType(boatDto.getBoatType());
        boat.setDestroyed(boatDto.isDestroyed());

        boat.setxHead(boatDto.getxHead());
        boat.setyHead(boatDto.getyHead());
        boat.setHorizontal(boatDto.isHorizontal());

        return boat;
    }

    public static ArrayList<BoatDTO> toDtos(ArrayList<Boat> boats) {
        return boats.stream().map(c -> toDto(c)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static BoatDTO toDto(Boat boat) {
        BoatDTO dto = new BoatDTO();
        dto.setBoatType(boat.getBoatType());
        dto.setDestroyed(boat.isDestroyed());

        dto.setxHead(boat.getxHead());
        dto.setyHead(boat.getyHead());
        dto.setHorizontal(boat.isHorizontal());

        return dto;

    }
}
