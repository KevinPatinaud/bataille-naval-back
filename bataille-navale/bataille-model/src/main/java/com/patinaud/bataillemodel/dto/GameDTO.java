package com.patinaud.bataillemodel.dto;

import com.patinaud.bataillemodel.constants.IdPlayer;

public class GameDTO {

    private String idGame;

    private IdPlayer idPlayerTurn;

    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public IdPlayer getIdPlayerTurn() {
        return idPlayerTurn;
    }

    public void setIdPlayerTurn(IdPlayer idPlayerTurn) {
        this.idPlayerTurn = idPlayerTurn;
    }

    public String toString() {
        return "idGame : " + getIdGame() + ", idPlayerTurn : " + getIdPlayerTurn();
    }
}
