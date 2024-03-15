package com.patinaud.batailleservice.service.persistence;

import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.Cell;

import java.util.ArrayList;

public interface PersistenceService {

    public ArrayList<Cell> getGrid(String idGame, IdPlayer idplayerToLoad);

    public CellContent revealeCell(String idGame, IdPlayer idPlayerTargeted, int xCellTargeted, int yCellTargeted);

    public ArrayList<Cell> getRevealedCells(String idGame, IdPlayer idPlayer);

    public boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer);

    public void updateStateBoats(String idGame, IdPlayer idPlayer);
}
