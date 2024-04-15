package com.patinaud.bataillecommunication.mapper;

import com.patinaud.bataillecommunication.response.Cell;
import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.dto.CellDTO;

import java.util.ArrayList;
import java.util.List;

public class CellsMapper {
    private CellsMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static ArrayList<Cell> fromDtosToResponses(List<CellDTO> dtoCells) {

        ArrayList<Cell> cells = new ArrayList<>();
        for (int i = 0; i < dtoCells.size(); i++) {
            Cell cell = new Cell();
            cell.setX(dtoCells.get(i).getX());
            cell.setY(dtoCells.get(i).getY());
            cell.setRevealed(dtoCells.get(i).isRevealed());

            cell.setCellContent(dtoCells.get(i).isOccupied() ? CellContent.BOAT : CellContent.NOTHING);

            cells.add(cell);
        }

        return cells;
    }


}
