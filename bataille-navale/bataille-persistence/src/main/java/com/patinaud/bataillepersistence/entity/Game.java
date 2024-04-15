package com.patinaud.bataillepersistence.entity;

import com.patinaud.bataillemodel.constants.GameMode;
import com.patinaud.bataillemodel.constants.IdPlayer;
import jakarta.persistence.*;

@Entity
public class Game {
    @Id
    @Column(name = "id")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "id_player_turn")
    private IdPlayer idPlayerTurn;


    @Enumerated(EnumType.STRING)
    @Column(name = "mode")
    private GameMode mode;

    public String getId() {
        return id;
    }

    public void setId(String idGame) {
        this.id = idGame;
    }

    public IdPlayer getIdPlayerTurn() {
        return idPlayerTurn;
    }

    public void setIdPlayerTurn(IdPlayer idPlayerTurn) {
        this.idPlayerTurn = idPlayerTurn;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }
}