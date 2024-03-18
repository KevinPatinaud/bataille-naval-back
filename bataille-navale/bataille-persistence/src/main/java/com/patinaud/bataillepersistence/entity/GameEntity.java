package com.patinaud.bataillepersistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // autres champs et méthodes


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}