package com.patinaud.batailleservice.service.gameengine;

public interface GameEngineService {
    public void playerAttack(String idGame, String idPlayerAttacker_in, int xTargeted, int yTargeted);

    public String generateNewGame();
}
