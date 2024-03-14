package com.patinaud.bataillemodel.constants;

public enum CellContent {
    NOTHING("nothing"),
    BOAT("boat");
    private final String value;

    CellContent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
