package com.patinaud.bataillepersistence.dao;


import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillepersistence.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, String> {
    @Modifying
    @Query("UPDATE Game SET idPlayerTurn = :idPlayerTurn WHERE id = :id")
    void updateIdPlayerTurn(@Param("id") String id, @Param("idPlayerTurn") IdPlayer idPlayerTurn);

}
