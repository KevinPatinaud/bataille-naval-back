package com.patinaud.batailleapi.mapper;

import com.patinaud.batailleapi.requestdata.Boat;
import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoatMapper {
    public static ArrayList<BoatDTO> toDtos(List<Boat> boats) {

        return boats.stream().map(b -> toDto(b)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static BoatDTO toDto(Boat boat) {

        BoatDTO dto = new BoatDTO();
        dto.setxHead(boat.getxHead());
        dto.setyHead(boat.getyHead());
        
        System.out.println("BoatMapper");
        System.out.println("boat.isHorizontal()");
        System.out.println(boat.isHorizontal());

        dto.setHorizontal(boat.isHorizontal());
        dto.setBoatType(getBoatType(boat.getType()));
        dto.setDestroyed(false);
        return dto;
    }

    private static BoatType getBoatType(String type) {
        if (type.equals("porte-avions"))
            return BoatType.PORTE_AVIONS;
        if (type.equals("croiseur"))
            return BoatType.CROISEUR;
        if (type.equals("sous-marin-1"))
            return BoatType.SOUS_MARIN_1;
        if (type.equals("sous-marin-2"))
            return BoatType.SOUS_MARIN_2;
        if (type.equals("torpilleur"))
            return BoatType.TORPILLEUR;

        return null;
    }

}
