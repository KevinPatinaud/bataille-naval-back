package com.patinaud.bataillecommunication.response;

import com.patinaud.bataillemodel.constants.BoatType;

public class BoatState {

    BoatType type;
    boolean isDestroyed;

    public BoatType getType() {
        return type;
    }

    public void setType(BoatType type) {
        this.type = type;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}
