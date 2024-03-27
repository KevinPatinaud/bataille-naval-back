package com.patinaud.bataillecommunication.responsedata;

import com.patinaud.bataillemodel.constants.IdPlayer;

import java.util.List;

public class PlayerCells {
    private IdPlayer idPlayer;
    private List<Cell> cells;

    public IdPlayer getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(IdPlayer idPlayer) {
        this.idPlayer = idPlayer;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
