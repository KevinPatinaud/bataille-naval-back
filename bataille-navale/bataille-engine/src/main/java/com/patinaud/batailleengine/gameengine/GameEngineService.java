package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.GameDTO;

import java.util.ArrayList;

public interface GameEngineService {

    GameDTO generateNewGame();

    void positionHumanPlayerBoat(String idGame, ArrayList<BoatDTO> boats);

    void playerAttack(String idGame, String idPlayerAttackerStr, int xTargeted, int yTargeted);

}
