package com.patinaud.bataillemodel.constants;

public enum BoatType {
    PORTE_AVIONS("porte-avions", 5),
    CROISEUR("croiseur", 4),
    SOUS_MARIN_1("sous-marin-1", 3),
    SOUS_MARIN_2("sous-marin-2", 3),
    TORPILLEUR("tropilleur", 2);


    private final String name;
    private final int size;

    BoatType(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
