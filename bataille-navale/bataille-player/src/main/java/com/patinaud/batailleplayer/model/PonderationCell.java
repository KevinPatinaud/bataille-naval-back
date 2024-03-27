package com.patinaud.batailleplayer.model;



public class PonderationCell {
    int weight;
    int x;
    int y;

    public PonderationCell(int weight, int x, int y) {
        this.weight = weight;
        this.x = x;
        this.y = y;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
