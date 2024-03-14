package com.patinaud.bataillemodel.constants;

public enum IdPlayer {
    PLAYER_1("player_1"),
    PLAYER_2("player_2");
    private final String value;

    IdPlayer(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(IdPlayer idPlayer2) {
        return this.getValue().equals(idPlayer2.getValue());
    }
}
