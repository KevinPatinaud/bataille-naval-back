package com.patinaud.bataillecommunication.responsedata;

import com.patinaud.bataillemodel.constants.IdPlayer;

import java.util.ArrayList;

public class PlayerCells {
    private IdPlayer idPlayer;
    private ArrayList<Cell> cells;

    public IdPlayer getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(IdPlayer idPlayer) {
        this.idPlayer = idPlayer;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }
}
