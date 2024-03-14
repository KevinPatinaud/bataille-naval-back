package com.patinaud.bataillemodel.constants;

public enum GameAction {
    REVEALED_CELLS("revealedCells");

    private final String value;

    GameAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
