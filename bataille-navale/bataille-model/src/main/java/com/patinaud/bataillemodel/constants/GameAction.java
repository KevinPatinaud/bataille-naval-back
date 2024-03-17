package com.patinaud.bataillemodel.constants;

public enum GameAction {
    REVEALED_CELLS("revealedCells"),
    BOATS_STATES("boatsStates"),
    END_GAME("endGame");

    private final String value;

    GameAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
