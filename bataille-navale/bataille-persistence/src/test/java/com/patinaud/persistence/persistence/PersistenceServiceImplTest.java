package com.patinaud.persistence.persistence;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
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
class PersistenceServiceImplTest {

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

        Assertions.assertEquals(100, cells.size());

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

        Assertions.assertEquals(100, cells.size());

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
        persistenceServiceImpl.revealCell("reveal_cell", IdPlayer.PLAYER_2, new CoordinateDTO(3, 5));
        ArrayList<CellDTO> revealedCells = persistenceServiceImpl.getRevealedCells("reveal_cell", IdPlayer.PLAYER_2);

        List<Cell> cells = cellRepository.findCells("reveal_cell", IdPlayer.PLAYER_2);

        Assertions.assertEquals(1, revealedCells.size());
        Assertions.assertEquals(3, revealedCells.get(0).getX());
        Assertions.assertEquals(5, revealedCells.get(0).getY());
    }

    @Test
    void revealCellOutsideGrid() {
        persistenceServiceImpl.initializeGame("reveal_cell");
        persistenceServiceImpl.revealCell("reveal_cell", IdPlayer.PLAYER_2, new CoordinateDTO(10, 5));
        ArrayList<CellDTO> revealedCells = persistenceServiceImpl.getRevealedCells("reveal_cell", IdPlayer.PLAYER_2);

        List<Cell> cells = cellRepository.findCells("reveal_cell", IdPlayer.PLAYER_2);

        Assertions.assertEquals(0, revealedCells.size());
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


    @Test
    void updateStateBoatsHorizontal() {
        persistenceServiceImpl.initializeGame("idGame");

        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.CROISEUR);
        boatDTO.setxHead(2);
        boatDTO.setyHead(4);
        boatDTO.setHorizontal(true);
        boatDTO.setDestroyed(false);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition("idGame", IdPlayer.PLAYER_1, boatsDtosInit);

        persistenceServiceImpl.revealCell("idGame", IdPlayer.PLAYER_1, new CoordinateDTO(2, 4));
        persistenceServiceImpl.updateStateBoats("idGame", IdPlayer.PLAYER_1);
        Boat croiseur = boatRepository.findBoatByType("idGame", IdPlayer.PLAYER_1, BoatType.CROISEUR);
        Assertions.assertFalse(croiseur.isDestroyed());


        persistenceServiceImpl.revealCell("idGame", IdPlayer.PLAYER_1, new CoordinateDTO(3, 4));
        persistenceServiceImpl.revealCell("idGame", IdPlayer.PLAYER_1, new CoordinateDTO(4, 4));
        persistenceServiceImpl.updateStateBoats("idGame", IdPlayer.PLAYER_1);
        croiseur = boatRepository.findBoatByType("idGame", IdPlayer.PLAYER_1, BoatType.CROISEUR);
        Assertions.assertFalse(croiseur.isDestroyed());


        persistenceServiceImpl.revealCell("idGame", IdPlayer.PLAYER_1, new CoordinateDTO(5, 4));
        persistenceServiceImpl.updateStateBoats("idGame", IdPlayer.PLAYER_1);
        croiseur = boatRepository.findBoatByType("idGame", IdPlayer.PLAYER_1, BoatType.CROISEUR);
        Assertions.assertTrue(croiseur.isDestroyed());


    }


    @Test
    void updateStateBoatsVertical() {
        persistenceServiceImpl.initializeGame("idGame");

        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.SOUS_MARIN_1);
        boatDTO.setxHead(3);
        boatDTO.setyHead(4);
        boatDTO.setHorizontal(false);
        boatDTO.setDestroyed(false);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition("idGame", IdPlayer.PLAYER_1, boatsDtosInit);

        persistenceServiceImpl.revealCell("idGame", IdPlayer.PLAYER_1, new CoordinateDTO(3, 4));
        persistenceServiceImpl.revealCell("idGame", IdPlayer.PLAYER_1, new CoordinateDTO(3, 5));
        persistenceServiceImpl.updateStateBoats("idGame", IdPlayer.PLAYER_1);
        Boat sousMarin1 = boatRepository.findBoatByType("idGame", IdPlayer.PLAYER_1, BoatType.SOUS_MARIN_1);
        Assertions.assertFalse(sousMarin1.isDestroyed());


        persistenceServiceImpl.revealCell("idGame", IdPlayer.PLAYER_1, new CoordinateDTO(3, 6));
        persistenceServiceImpl.updateStateBoats("idGame", IdPlayer.PLAYER_1);
        sousMarin1 = boatRepository.findBoatByType("idGame", IdPlayer.PLAYER_1, BoatType.SOUS_MARIN_1);
        Assertions.assertTrue(sousMarin1.isDestroyed());

    }

    @Test
    void revealCellsNextToDestroyedBoatVertical() {

        persistenceServiceImpl.initializeGame("idGame");

        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.SOUS_MARIN_1);
        boatDTO.setxHead(3);
        boatDTO.setyHead(4);
        boatDTO.setHorizontal(false);
        boatDTO.setDestroyed(true);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition("idGame", IdPlayer.PLAYER_1, boatsDtosInit);

        persistenceServiceImpl.revealCellsNextToDestroyedBoat("idGame", IdPlayer.PLAYER_1);

        Assertions.assertTrue(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 3, 4).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 2, 3).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 4, 3).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 2, 7).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 4, 7).isRevealed());

        Assertions.assertFalse(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 5, 7).isRevealed());
        Assertions.assertFalse(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 1, 7).isRevealed());

    }


    @Test
    void revealCellsNextToDestroyedBoatHorizontal() {

        persistenceServiceImpl.initializeGame("idGame");

        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.PORTE_AVIONS);
        boatDTO.setxHead(1);
        boatDTO.setyHead(1);
        boatDTO.setHorizontal(true);
        boatDTO.setDestroyed(true);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition("idGame", IdPlayer.PLAYER_1, boatsDtosInit);

        persistenceServiceImpl.revealCellsNextToDestroyedBoat("idGame", IdPlayer.PLAYER_1);

        Assertions.assertTrue(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 0, 0).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 6, 0).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 0, 2).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 6, 2).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 2, 1).isRevealed());

        Assertions.assertFalse(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 6, 4).isRevealed());
        Assertions.assertFalse(cellRepository.findCellXY("idGame", IdPlayer.PLAYER_1, 5, 4).isRevealed());

    }

}
