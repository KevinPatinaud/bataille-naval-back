package com.patinaud.batailleapi.request;

import com.patinaud.bataillemodel.constants.GameMode;

public class GameRequest {
    private GameMode mode;

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }
}
