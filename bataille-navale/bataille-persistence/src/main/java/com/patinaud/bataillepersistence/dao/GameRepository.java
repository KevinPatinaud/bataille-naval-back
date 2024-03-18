package com.patinaud.bataillepersistence.dao;


import com.patinaud.bataillepersistence.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

}
