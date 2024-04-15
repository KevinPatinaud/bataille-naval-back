package com.patinaud.bataillecommunication.response;

import java.util.List;

public class PlayerState {

    List<BoatState> boatsStates;
    private List<Cell> cells;


    public List<BoatState> getBoatsStates() {
        return boatsStates;
    }

    public void setBoatsStates(List<BoatState> boatsStates) {
        this.boatsStates = boatsStates;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
