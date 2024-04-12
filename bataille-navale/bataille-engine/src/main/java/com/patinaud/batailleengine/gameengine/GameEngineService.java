package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillemodel.constants.GameMode;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import com.patinaud.bataillemodel.dto.GameDTO;

import java.util.List;

public interface GameEngineService {


    String generateIdGame();

    GameDTO generateNewGame(GameMode gameMode) throws Exception;

    boolean isGameWaitingSecondPlayer(String idGame);

    void positionHumanPlayerBoat(String idGame, IdPlayer idPlayer, List<BoatDTO> boats);

    void playerAttack(String idGame, String idPlayerAttackerStr, CoordinateDTO coordinateTargeted);

}
