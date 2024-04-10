package com.patinaud.batailleapi.requestdata;

public class Game {
    private String idGame;

    public Game() {
    }

    public Game(String idGame) {
        this.idGame = idGame;
    }

    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }
}
