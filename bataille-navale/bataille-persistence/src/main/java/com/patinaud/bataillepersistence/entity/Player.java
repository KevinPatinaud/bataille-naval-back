package com.patinaud.bataillepersistence.entity;

import com.patinaud.bataillemodel.constants.IdPlayer;
import jakarta.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Long uid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game")
    private Game game;

    
    @Column(name = "id_player")
    private IdPlayer idPlayer;

    @Column(name = "is_ia")
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public boolean isIA() {
        return isIA;
    }

    public void setIA(boolean IA) {
        isIA = IA;
    }
}
