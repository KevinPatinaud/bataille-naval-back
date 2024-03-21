package com.patinaud.bataillepersistence.mapper;

import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillepersistence.entity.Cell;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CellMapper {
    private CellMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static ArrayList<CellDTO> toDtos(ArrayList<Cell> cells) {
        return cells.stream().map(c -> toDto(c)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static CellDTO toDto(Cell cell) {
        CellDTO cellDTO = new CellDTO();
        cellDTO.setX(cell.getX());
        cellDTO.setY(cell.getY());
        cellDTO.setRevealed(cell.isRevealed());
        cellDTO.setOccupied(cell.isOccupied());
        return cellDTO;
    }


}
