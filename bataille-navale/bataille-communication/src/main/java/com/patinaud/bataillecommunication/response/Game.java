package com.patinaud.bataillecommunication.response;

import com.patinaud.bataillemodel.constants.GameMode;
import com.patinaud.bataillemodel.constants.IdPlayer;

public class Game {


    String id;

    GameMode mode;

    IdPlayer idPlayerTurn;

    PlayerState player1;

    PlayerState player2;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public IdPlayer getIdPlayerTurn() {
        return idPlayerTurn;
    }

    public void setIdPlayerTurn(IdPlayer idPlayerTurn) {
        this.idPlayerTurn = idPlayerTurn;
    }

    public PlayerState getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerState player1) {
        this.player1 = player1;
    }

    public PlayerState getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerState player2) {
        this.player2 = player2;
    }
}
