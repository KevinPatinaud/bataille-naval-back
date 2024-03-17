package com.patinaud.bataillecommunication.mapper;

import com.patinaud.bataillecommunication.responsedata.Cell;
import com.patinaud.bataillecommunication.responsedata.PlayerCells;
import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;

import java.util.ArrayList;
import java.util.List;

public class PlayerCellsMapper {
    public static PlayerCells fromDtoToResponse(IdPlayer idplayer, ArrayList<CellDTO> dtoCells, ArrayList<BoatDTO> boats) {
        PlayerCells playerCells = new PlayerCells();

        playerCells.setIdPlayer(idplayer);

        ArrayList<Cell> cells = new ArrayList<>();
        for (int i = 0; i < dtoCells.size(); i++) {
            Cell cell = new Cell();
            cell.setX(dtoCells.get(i).getX());
            cell.setY(dtoCells.get(i).getY());

            cell.setCellContent(getCellContent(boats, cell.getX(), cell.getY()));

            cells.add(cell);
        }

        playerCells.setCells(cells);

        return playerCells;
    }


    public static CellContent getCellContent(ArrayList<BoatDTO> boats, int x, int y) {

        for (int i = 0; i < boats.size(); i++) {
            BoatDTO boat = boats.get(i);

            int xBoat = boat.getxHead();
            int yBoat = boat.getyHead();

            for (int inc = 0; inc < boat.getBoatType().getSize(); inc++) {
                if (x == xBoat + (boat.isHorizontal() ? inc : 0) && y == yBoat + (!boat.isHorizontal() ? inc : 0)) {
                    return CellContent.BOAT;
                }
            }

        }

        return CellContent.NOTHING;
    }
}
