package com.patinaud.bataillecommunication.responsedata;

import com.patinaud.bataillemodel.constants.IdPlayer;


import java.util.List;

public class PlayerBoatsStates {

    IdPlayer idPlayer;

    List<BoatState> boatsStates;

    public IdPlayer getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(IdPlayer idPlayer) {
        this.idPlayer = idPlayer;
    }

    public List<BoatState> getBoatsStates() {
        return boatsStates;
    }

    public void setBoatsStates(List<BoatState> boatsStates) {
        this.boatsStates = boatsStates;
    }
}
