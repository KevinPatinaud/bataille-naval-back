package com.patinaud.bataillepersistence.entity;

import com.patinaud.bataillemodel.constants.BoatType;
import jakarta.persistence.*;

@Entity
public class Boat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player")
    private Player player;

    @Column(name = "x_head")
    private int xHead;

    @Column(name = "y_head")
    private int yHead;
    @Column(name = "is_horizontal")
    private boolean isHorizontal;
    @Column(name = "boat_type")
    private BoatType boatType;

    @Column(name = "is_destroyed")
    private boolean isDestroyed;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getxHead() {
        return xHead;
    }

    public void setxHead(int xHead) {
        this.xHead = xHead;
    }

    public int getyHead() {
        return yHead;
    }

    public void setyHead(int yHead) {
        this.yHead = yHead;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public BoatType getBoatType() {
        return boatType;
    }

    public void setBoatType(BoatType boatType) {
        this.boatType = boatType;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}
