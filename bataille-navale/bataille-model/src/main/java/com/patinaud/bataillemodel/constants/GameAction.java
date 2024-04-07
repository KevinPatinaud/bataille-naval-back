package com.patinaud.bataillemodel.constants;

public enum GameAction {
    GRID("grid"),
    BOATS("boats"),
    END_GAME("endGame");

    private final String value;

    GameAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
