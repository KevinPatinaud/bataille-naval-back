package com.patinaud.batailleapi.request;

import com.patinaud.bataillemodel.constants.GameMode;

public class GameRequest {
    private String id;

    private GameMode mode;

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
}
