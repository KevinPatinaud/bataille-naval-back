package com.patinaud.bataillepersistence.mapper;

import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import com.patinaud.bataillepersistence.entity.Cell;
import com.patinaud.bataillepersistence.entity.Player;

import java.util.*;

public class GridMapper {
    private GridMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static GridDTO toDto(List<Cell> cells) {

        List<Cell> sortedCells = cells.stream()
                .sorted(Comparator.comparing(Cell::getY).thenComparing(Cell::getX))
                .toList();


        List<CellDTO> lineCells = new ArrayList<>();

        List<List<CellDTO>> gridCells = new ArrayList<>();

        int previousLine = sortedCells.get(0).getY();

        for (int i = 0; i < sortedCells.size(); i++) {

            CellDTO cell = CellMapper.toDto(sortedCells.get(i));

            if (cell.getY() != previousLine) {
                gridCells.add(lineCells);
                lineCells = new ArrayList<>();
                previousLine = cell.getY();
            }

            lineCells.add(cell);

        }
        gridCells.add(lineCells);

        GridDTO grid = new GridDTO();
        grid.setCells(gridCells);
        return grid;
    }


    public static List<Cell> toCellsEntities(GridDTO grid, Player player) {

        return grid.getCells().stream().flatMap(List::stream).map(cellDTO -> CellMapper.toEntity(cellDTO, player)).toList();

    }
}
