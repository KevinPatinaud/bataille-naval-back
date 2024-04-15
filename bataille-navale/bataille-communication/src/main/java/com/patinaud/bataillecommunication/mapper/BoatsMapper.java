package com.patinaud.bataillecommunication.mapper;

import com.patinaud.bataillecommunication.response.BoatState;
import com.patinaud.bataillemodel.dto.BoatDTO;

import java.util.ArrayList;
import java.util.List;

public class BoatsMapper {
    private BoatsMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static List<BoatState> fromDtoToResponse(List<BoatDTO> boatsDtos) {

        ArrayList<BoatState> boatsStates = new ArrayList<>();

        for (int i = 0; i < boatsDtos.size(); i++) {
            BoatState boatState = new BoatState();
            boatState.setType(boatsDtos.get(i).getBoatType());
            boatState.setDestroyed(boatsDtos.get(i).isDestroyed());
            boatsStates.add(boatState);
        }

        return boatsStates;
    }
}
