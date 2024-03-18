package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillemodel.dto.GameDTO;

public interface GameEngineService {
    public void playerAttack(String idGame, String idPlayerAttacker_in, int xTargeted, int yTargeted);

    public GameDTO generateNewGame();
}
