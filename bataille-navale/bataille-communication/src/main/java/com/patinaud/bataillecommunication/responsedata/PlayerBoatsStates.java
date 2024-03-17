package com.patinaud.bataillecommunication.responsedata;

import com.patinaud.bataillemodel.constants.IdPlayer;

import java.util.ArrayList;

public class PlayerBoatsStates {

    IdPlayer idPlayer;

    ArrayList<BoatState> boatsStates;

    public IdPlayer getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(IdPlayer idPlayer) {
        this.idPlayer = idPlayer;
    }

    public ArrayList<BoatState> getBoatsStates() {
        return boatsStates;
    }

    public void setBoatsStates(ArrayList<BoatState> boatsStates) {
        this.boatsStates = boatsStates;
    }
}
