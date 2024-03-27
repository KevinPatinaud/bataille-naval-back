package com.patinaud.bataillepersistence.mapper;

import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillepersistence.entity.Cell;
import com.patinaud.bataillepersistence.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CellMapper {
    private CellMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static List<CellDTO> toDtos(ArrayList<Cell> cells) {
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

    public static Cell toEntity(CellDTO cellDTO, Player player) {
        Cell cell = new Cell();
        cell.setX(cellDTO.getX());
        cell.setY(cellDTO.getY());
        cell.setRevealed(cellDTO.isRevealed());
        cell.setOccupied(cellDTO.isOccupied());
        cell.setPlayer(player);
        return cell;
    }

}
