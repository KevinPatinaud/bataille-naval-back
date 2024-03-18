package com.patinaud.bataillepersistence.dao;


import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillepersistence.entity.Boat;
import com.patinaud.bataillepersistence.entity.Cell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface BoatRepository extends JpaRepository<Boat, Long> {
    @Query(value = "select BOAT.*  from PLAYER, BOAT where PLAYER.uid=BOAT.player and PLAYER.game=:idGame and PLAYER.id_player=:idPlayer ", nativeQuery = true)
    ArrayList<Boat> findBoats(@Param("idGame") String idGame, @Param("idPlayer") IdPlayer idPlayer);

    @Query(value = "select BOAT.*  from PLAYER, BOAT where PLAYER.uid=BOAT.player and PLAYER.game=:idGame and PLAYER.id_player=:idPlayer and BOAT.is_destroyed=:isDestroyed ", nativeQuery = true)
    ArrayList<Boat> findBoatsByDestroyedState(@Param("idGame") String idGame, @Param("idPlayer") IdPlayer idPlayer, @Param("isDestroyed") boolean isDestroyed);

    @Query(value = "select BOAT.*  from PLAYER, BOAT where PLAYER.uid=BOAT.player and PLAYER.game=:idGame and PLAYER.id_player=:idPlayer and  BOAT.boat_type=:boatType", nativeQuery = true)
    Boat findBoatByType(@Param("idGame") String idGame, @Param("idPlayer") IdPlayer idPlayer, @Param("boatType") BoatType boatType);
}
