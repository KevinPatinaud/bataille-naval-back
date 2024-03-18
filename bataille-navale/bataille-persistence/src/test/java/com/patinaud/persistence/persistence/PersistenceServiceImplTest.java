package com.patinaud.persistence.persistence;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillepersistence.dao.BoatRepository;
import com.patinaud.bataillepersistence.dao.CellRepository;
import com.patinaud.bataillepersistence.dao.GameRepository;
import com.patinaud.bataillepersistence.dao.PlayerRepository;
import com.patinaud.bataillepersistence.entity.Boat;
import com.patinaud.bataillepersistence.entity.Cell;
import com.patinaud.bataillepersistence.entity.Player;
import com.patinaud.bataillepersistence.persistence.PersistenceServiceImpl;
import com.patinaud.persistence.config.TestDatabaseConfiguration;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDatabaseConfiguration.class})
@DirtiesContext
@Transactional
public class PersistenceServiceImplTest {

    @Autowired
    PersistenceServiceImpl persistenceServiceImpl;


    @Autowired
    GameRepository gameRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    CellRepository cellRepository;

    @Autowired
    BoatRepository boatRepository;

    @Test
    void initializeCreateANewGame() {
        persistenceServiceImpl.initializeGame("ABCDE");
        Assertions.assertEquals(1, gameRepository.findAll().size());
        Assertions.assertTrue(gameRepository.findById("ABCDE").isPresent());
        Assertions.assertEquals("ABCDE", gameRepository.findById("ABCDE").get().getIdGame());
        Assertions.assertTrue(gameRepository.findById("ABCDE").get().getIdPlayerTurn().equals(IdPlayer.PLAYER_1));
        Assertions.assertFalse(gameRepository.findById("FGH").isPresent());
    }

    @Test
    void initializeCreateTwiceTheSameGameId() {
        persistenceServiceImpl.initializeGame("ABCDE");
        persistenceServiceImpl.initializeGame("ABCDE");
        Assertions.assertEquals(1, gameRepository.findAll().size());
        Assertions.assertTrue(gameRepository.findById("ABCDE").isPresent());
        Assertions.assertFalse(gameRepository.findById("FGH").isPresent());
    }


    @Test
    void initializeCreatePlayer1() {
        persistenceServiceImpl.initializeGame("ABCDE");
        Player player = playerRepository.findByGame("ABCDE", IdPlayer.PLAYER_1);
        Assertions.assertEquals("ABCDE", player.getGame().getIdGame());
        Assertions.assertEquals(IdPlayer.PLAYER_1, player.getIdPlayer());
        Assertions.assertFalse(player.isIA());
    }

    @Test
    void initializeCreatePlayer1Cells() {
        persistenceServiceImpl.initializeGame("ABCDE");
        List<Cell> cells = cellRepository.findCells("ABCDE", IdPlayer.PLAYER_1);

        Assertions.assertEquals(cells.size(), 100);

        for (int i = 0; i < cells.size(); i++) {
            Assertions.assertFalse(cells.get(i).isRevealed());
        }
    }

    @Test
    void initializeCreatePlayer2() {
        persistenceServiceImpl.initializeGame("ABCDE");
        Player player = playerRepository.findByGame("ABCDE", IdPlayer.PLAYER_2);
        Assertions.assertTrue(player.getIdPlayer().equals(IdPlayer.PLAYER_2));
    }


    @Test
    void initializeCreatePlayer2Cells() {
        persistenceServiceImpl.initializeGame("ABCDE");
        List<Cell> cells = cellRepository.findCells("ABCDE", IdPlayer.PLAYER_2);

        Assertions.assertEquals(cells.size(), 100);

        for (int i = 0; i < cells.size(); i++) {
            Assertions.assertFalse(cells.get(i).isRevealed());
        }
    }

    @Test
    void revealCellNoCellsRevealed() {
        persistenceServiceImpl.initializeGame("reveal_cell");
        ArrayList<CellDTO> revealedCells = persistenceServiceImpl.getRevealedCells("reveal_cell", IdPlayer.PLAYER_1);

        Assertions.assertTrue(revealedCells.isEmpty());
    }

    @Test
    void revealCell() {
        persistenceServiceImpl.initializeGame("reveal_cell");
        persistenceServiceImpl.revealeCell("reveal_cell", IdPlayer.PLAYER_2, 3, 5);
        ArrayList<CellDTO> revealedCells = persistenceServiceImpl.getRevealedCells("reveal_cell", IdPlayer.PLAYER_2);

        List<Cell> cells = cellRepository.findCells("reveal_cell", IdPlayer.PLAYER_2);

        Assertions.assertEquals(1, revealedCells.size());
        Assertions.assertEquals(3, revealedCells.get(0).getX());
        Assertions.assertEquals(5, revealedCells.get(0).getY());
    }


    @Test
    void setBoatPosition() {
        persistenceServiceImpl.initializeGame("idGame");
        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.TORPILLEUR);
        boatDTO.setxHead(2);
        boatDTO.setyHead(4);
        boatDTO.setHorizontal(true);
        boatDTO.setDestroyed(false);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition("idGame", IdPlayer.PLAYER_1, boatsDtosInit);

        ArrayList<BoatDTO> boats = persistenceServiceImpl.getBoats("idGame", IdPlayer.PLAYER_1);
        System.out.println(boats);

        Assertions.assertEquals(1, boats.size());
        BoatDTO torpilleur = boats.get(0);
        Assertions.assertEquals(BoatType.TORPILLEUR, torpilleur.getBoatType());
        Assertions.assertEquals(2, torpilleur.getxHead());
        Assertions.assertEquals(4, torpilleur.getyHead());
        Assertions.assertTrue(torpilleur.isHorizontal());
        Assertions.assertFalse(torpilleur.isDestroyed());
    }


    @Test
    void isAllBoatDestroyedFalse() {
        persistenceServiceImpl.initializeGame("idGame");
        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.TORPILLEUR);
        boatDTO.setxHead(2);
        boatDTO.setyHead(4);
        boatDTO.setHorizontal(true);
        boatDTO.setDestroyed(false);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition("idGame", IdPlayer.PLAYER_1, boatsDtosInit);

        ArrayList<BoatDTO> boats = persistenceServiceImpl.getBoats("idGame", IdPlayer.PLAYER_1);
        Assertions.assertFalse(persistenceServiceImpl.isAllBoatDestroyed("idGame", IdPlayer.PLAYER_1));
    }


}
