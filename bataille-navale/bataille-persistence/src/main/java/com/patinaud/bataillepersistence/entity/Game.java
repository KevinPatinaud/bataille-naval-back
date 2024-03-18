package com.patinaud.bataillepersistence.entity;

import jakarta.persistence.*;

@Entity
public class Game {
    @Id
    @Column(name = "id_game")
    private String idGame;


    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

}