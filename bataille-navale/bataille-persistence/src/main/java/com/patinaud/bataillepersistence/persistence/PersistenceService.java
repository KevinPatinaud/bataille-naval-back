package com.patinaud.bataillepersistence.persistence;


import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;

import java.util.ArrayList;

public interface PersistenceService {

    public void initializeGame(String idGame);

    public void revealeCell(String idGame, IdPlayer idPlayerTargeted, int xCellTargeted, int yCellTargeted);

    public ArrayList<CellDTO> getRevealedCells(String idGame, IdPlayer idPlayer);

    public ArrayList<BoatDTO> getBoats(String idGame, IdPlayer idPlayer);

    public boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer);

    public void updateStateBoats(String idGame, IdPlayer idPlayer);

    public void setBoatPosition(String idGame, IdPlayer idPlayer, ArrayList<BoatDTO> positionBoatOnGrid);
}
