package com.patinaud.bataillecommunication.responsedata;

import com.patinaud.bataillemodel.constants.CellContent;

public class Cell {

    private int x;
    private int y;

    private boolean isRevealed;

    private CellContent cellContent;

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

    public CellContent getCellContent() {
        return cellContent;
    }

    public void setCellContent(CellContent cellContent) {
        this.cellContent = cellContent;
    }
}
