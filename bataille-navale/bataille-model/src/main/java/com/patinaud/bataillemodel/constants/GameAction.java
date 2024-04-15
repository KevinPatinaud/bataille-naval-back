package com.patinaud.bataillemodel.constants;

public enum GameAction {
    PLAYER_JOIN("playerJoin"),
    PLAYER_POSITION_BOAT("playerPositionBoats"),
    GAME_STATE("gameState"),

    END_GAME("endGame");

    private final String value;

    GameAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
