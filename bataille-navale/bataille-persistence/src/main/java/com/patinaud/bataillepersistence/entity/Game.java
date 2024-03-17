package com.patinaud.bataillepersistence.entity;

import com.patinaud.bataillemodel.constants.IdPlayer;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jdk.jfr.Name;

@Entity
public class Game {
    @Id
    @Name("idGame")
    private String idGame;

    @Name("idPlayerWhoShouldPlay")
    private IdPlayer idPlayerWhoShouldPlay;


}
