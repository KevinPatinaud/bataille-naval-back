package com.patinaud.bataillecommunication.mapper;

import com.patinaud.bataillecommunication.responsedata.Cell;
import com.patinaud.bataillecommunication.responsedata.PlayerCells;
import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.CellDTO;

import java.util.ArrayList;

public class PlayerCellsMapper {
    private PlayerCellsMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static PlayerCells fromDtosToResponses(IdPlayer idplayer, ArrayList<CellDTO> dtoCells) {
        PlayerCells playerCells = new PlayerCells();

        playerCells.setIdPlayer(idplayer);

        ArrayList<Cell> cells = new ArrayList<>();
        for (int i = 0; i < dtoCells.size(); i++) {
            Cell cell = new Cell();
            cell.setX(dtoCells.get(i).getX());
            cell.setY(dtoCells.get(i).getY());

            cell.setCellContent(dtoCells.get(i).isOccupied() ? CellContent.BOAT : CellContent.NOTHING);

            cells.add(cell);
        }

        playerCells.setCells(cells);

        return playerCells;
    }


}
