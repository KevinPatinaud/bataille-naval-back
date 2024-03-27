package com.patinaud.bataillemodel.dto;

import com.patinaud.bataillemodel.constants.IdPlayer;

public class PlayerDTO {
    private GameDTO game;

    private IdPlayer idPlayer;

    private boolean isIA;

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public IdPlayer getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(IdPlayer idPlayer) {
        this.idPlayer = idPlayer;
    }

    public boolean isIA() {
        return isIA;
    }

    public void setIA(boolean isIA) {
        this.isIA = isIA;
    }
}
