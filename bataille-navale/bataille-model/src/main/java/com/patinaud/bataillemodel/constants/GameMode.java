package com.patinaud.bataillemodel.constants;

public enum GameMode {
    SOLO("solo"),
    MULTI_PLAYER("multi_player");

    private final String value;

    GameMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
