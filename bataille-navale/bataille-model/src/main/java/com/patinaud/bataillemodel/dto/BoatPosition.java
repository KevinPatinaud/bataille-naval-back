package com.patinaud.bataillemodel.dto;

import com.patinaud.bataillemodel.constants.BoatType;

public class BoatPosition {
    private int xHead;
    private int yHead;

    private BoatType boatType;

    private boolean isHorizontal;

    private boolean isDestroyed;

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

    public BoatType getBoatType() {
        return boatType;
    }

    public void setBoatType(BoatType boatType) {
        this.boatType = boatType;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}
