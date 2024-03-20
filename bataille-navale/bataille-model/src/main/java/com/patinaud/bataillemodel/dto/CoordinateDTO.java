package com.patinaud.bataillemodel.dto;

public class CoordinateDTO {
    private int x;
    private int y;

    public CoordinateDTO() {
    }

    public CoordinateDTO(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
