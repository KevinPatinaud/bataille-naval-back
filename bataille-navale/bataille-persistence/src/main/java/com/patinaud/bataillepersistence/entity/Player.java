package com.patinaud.bataillepersistence.entity;

import com.patinaud.bataillemodel.constants.IdPlayer;
import jakarta.persistence.*;
import jdk.jfr.Name;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Name("uid")
    private Long uid;

    @Name("id_player")
    private IdPlayer idPlayer;

    @Name("should_play")
    private boolean shouldPlay;
    @Name("is_ia")
    private boolean isIA;


    public Long getUid() {
        return uid;
    }


    public IdPlayer getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(IdPlayer idPlayer) {
        this.idPlayer = idPlayer;
    }


    public boolean isShouldPlay() {
        return shouldPlay;
    }

    public void setShouldPlay(boolean shouldPlay) {
        this.shouldPlay = shouldPlay;
    }

    public boolean isIA() {
        return isIA;
    }

    public void setIA(boolean IA) {
        isIA = IA;
    }
}
