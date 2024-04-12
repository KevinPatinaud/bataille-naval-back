package com.patinaud.batailleapi.response;

import com.patinaud.bataillemodel.constants.GameMode;
import com.patinaud.bataillemodel.constants.IdPlayer;

public class Game {
    private String id;

    private GameMode mode;

    private IdPlayer idPlayerTurn;


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
}
