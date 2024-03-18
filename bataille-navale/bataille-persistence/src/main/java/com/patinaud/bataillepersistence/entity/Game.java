package com.patinaud.bataillepersistence.entity;

import com.patinaud.bataillemodel.constants.IdPlayer;
import jakarta.persistence.*;

@Entity
public class Game {
    @Id
    @Column(name = "id_game")
    private String idGame;

    @Enumerated(EnumType.STRING)
    @Column(name = "id_player_turn")
    private IdPlayer idPlayerTurn;

    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public IdPlayer getIdPlayerTurn() {
        return idPlayerTurn;
    }

    public void setIdPlayerTurn(IdPlayer idPlayerTurn) {
        this.idPlayerTurn = idPlayerTurn;
    }
}