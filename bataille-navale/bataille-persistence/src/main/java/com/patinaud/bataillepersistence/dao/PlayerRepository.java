package com.patinaud.bataillepersistence.dao;

import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillepersistence.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player, String> {

    @Query(value = "select *  from PLAYER where game=:idGame and id_player=:idPlayer", nativeQuery = true)
    Player findByGame(@Param("idGame") String idGame, @Param("idPlayer") IdPlayer idPlayer);
}
