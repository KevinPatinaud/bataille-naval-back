package com.patinaud.bataillepersistence.dao;

import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillepersistence.entity.Cell;
import com.patinaud.bataillepersistence.entity.Game;
import com.patinaud.bataillepersistence.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;


public interface CellRepository extends JpaRepository<Cell, Long> {

    @Query(value = "select CELL.*  from PLAYER, CELL where PLAYER.uid=CELL.player and PLAYER.game=:idGame and PLAYER.id_player=:idPlayer ", nativeQuery = true)
    ArrayList<Cell> findCells(@Param("idGame") String idGame, @Param("idPlayer") IdPlayer idPlayer);

    @Query(value = "select CELL.*  from PLAYER, CELL where PLAYER.uid=CELL.player and PLAYER.game=:idGame and PLAYER.id_player=:idPlayer and CELL.is_revealed=true", nativeQuery = true)
    ArrayList<Cell> findRevealedCells(@Param("idGame") String idGame, @Param("idPlayer") IdPlayer idPlayer);

    @Query(value = "select CELL.*  from PLAYER, CELL where PLAYER.uid=CELL.player and PLAYER.game=:idGame and PLAYER.id_player=:idPlayer and CELL.x=:x and CELL.y=:y", nativeQuery = true)
    Cell findCellXY(@Param("idGame") String idGame, @Param("idPlayer") IdPlayer idPlayer, @Param("x") int x, @Param("y") int y);


/*
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE  CELL   SET CELL.is_revealed=true  FROM PLAYER from where PLAYER.uid=CELL.player and PLAYER.game=:idGame and PLAYER.id_player=:idPlayer and CELL.x=:x and CELL.y=:y ", nativeQuery = true)
    void revealeCell(String idGame, IdPlayer idPlayer, int x, int y);
    */


}
