package com.patinaud.batailleapi.mapper;

import com.patinaud.batailleapi.requestdata.Coordinate;
import com.patinaud.bataillemodel.dto.CoordinateDTO;

public class CoordinateMapper {
    private CoordinateMapper() {
        throw new IllegalStateException("Utility class");
    }
    public static CoordinateDTO toDto(Coordinate coordinate) {
        return new CoordinateDTO(coordinate.getX(), coordinate.getY());
    }
}
