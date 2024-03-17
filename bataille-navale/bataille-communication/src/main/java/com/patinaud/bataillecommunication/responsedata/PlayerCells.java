package com.patinaud.bataillecommunication.responsedata;

import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.CellDTO;

import java.util.ArrayList;
import java.util.List;

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
