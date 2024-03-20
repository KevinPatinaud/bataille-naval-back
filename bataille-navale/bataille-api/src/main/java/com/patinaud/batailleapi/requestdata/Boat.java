package com.patinaud.batailleapi.requestdata;

public class Boat {

    private int xHead;
    private int yHead;
    private boolean isHorizontal;
    private String type;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
