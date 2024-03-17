package com.patinaud.bataillemodel.dto;

import com.patinaud.bataillemodel.constants.CellContent;

public class CellDTO {
    private int x;
    private int y;

    private boolean isRevealed;

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


    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }
}
