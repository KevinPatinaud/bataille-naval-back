package com.patinaud.bataillepersistence.persistence;


import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.*;


import java.util.List;

public interface PersistenceService {

    public void saveGrid(String idGame, IdPlayer idPlayer, GridDTO grid);

    public GridDTO getGrid(String idGame, IdPlayer idPlayer);

    public void revealCell(String idGame, IdPlayer idPlayer, CoordinateDTO coordinate);

    public List<CellDTO> getRevealedCells(String idGame, IdPlayer idPlayer);

    public void saveGame(GameDTO gameDto);

    public void savePlayer(PlayerDTO playerDto);

    public List<BoatDTO> getBoats(String idGame, IdPlayer idPlayer);

    public boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer);

    public void updateStateBoats(String idGame, IdPlayer idPlayer);

    public void setBoatPosition(String idGame, IdPlayer idPlayer, List<BoatDTO> positionBoatOnGrid);

    public void revealCellsNextToDestroyedBoat(String idGame, IdPlayer idPlayer);
}
