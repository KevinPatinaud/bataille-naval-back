package com.patinaud.bataillepersistence.persistence;


import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.*;

import java.util.List;

public interface PersistenceService {


    void saveGrid(String idGame, IdPlayer idPlayer, GridDTO grid);

    GridDTO getGrid(String idGame, IdPlayer idPlayer);

    void revealCell(String idGame, IdPlayer idPlayer, CoordinateDTO coordinate);

    List<CellDTO> getRevealedCells(String idGame, IdPlayer idPlayer);

    boolean isGameExist(String idGame);

    void saveGame(GameDTO gameDto);

    void savePlayer(PlayerDTO playerDto);

    List<BoatDTO> getBoats(String idGame, IdPlayer idPlayer);

    boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer);

    void updateStateBoats(String idGame, IdPlayer idPlayer);

    void setBoatPosition(String idGame, IdPlayer idPlayer, List<BoatDTO> positionBoatOnGrid);

    void revealCellsNextToDestroyedBoat(String idGame, IdPlayer idPlayer);

    boolean userExistByEmail(String email);

    UserDTO registerUser(UserDTO user);
}
