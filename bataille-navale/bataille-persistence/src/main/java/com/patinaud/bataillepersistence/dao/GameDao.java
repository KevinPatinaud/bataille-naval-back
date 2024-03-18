package com.patinaud.bataillepersistence.dao;


import com.patinaud.bataillepersistence.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDao extends JpaRepository<GameEntity, Long> {

}
