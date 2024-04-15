package com.patinaud.persistence.persistence;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.*;
import com.patinaud.bataillepersistence.dao.BoatRepository;
import com.patinaud.bataillepersistence.dao.CellRepository;
import com.patinaud.bataillepersistence.dao.GameRepository;
import com.patinaud.bataillepersistence.dao.PlayerRepository;
import com.patinaud.bataillepersistence.entity.Boat;
import com.patinaud.bataillepersistence.entity.Cell;
import com.patinaud.bataillepersistence.persistence.PersistenceGameServiceImpl;
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
class PersistenceGameServiceImplTest {

    @Autowired
    PersistenceGameServiceImpl persistenceServiceImpl;


    @Autowired
    GameRepository gameRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    CellRepository cellRepository;

    @Autowired
    BoatRepository boatRepository;


    private GridDTO generateEmptyGrid(int width, int height) {

        List<List<CellDTO>> grid = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            List<CellDTO> line = new ArrayList<>();
            for (int y = 0; y < height; y++) {
                CellDTO cell = new CellDTO();
                cell.setX(x);
                cell.setY(y);
                cell.setRevealed(false);
                cell.setOccupied(false);
                line.add(cell);
            }
            grid.add(line);
        }
        GridDTO gridDto = new GridDTO();
        gridDto.setCells(grid);
        return gridDto;
    }

    private void initGame(String idGame, IdPlayer idPlayer) {

        GameDTO game = new GameDTO();
        game.setId(idGame);


        PlayerDTO player = new PlayerDTO();
        player.setIA(false);
        player.setIdPlayer(idPlayer);
        player.setGame(game);

        persistenceServiceImpl.saveGame(game);
        persistenceServiceImpl.savePlayer(player);

        persistenceServiceImpl.saveGrid(game.getId(), player.getIdPlayer(), generateEmptyGrid(10, 10));
    }

    @Test
    void getGrid() {

        String idGame = "reveal_cell";
        IdPlayer idPlayer = IdPlayer.PLAYER_1;
        initGame(idGame, idPlayer);

        GridDTO grid = persistenceServiceImpl.getGrid(idGame, idPlayer);

        Assertions.assertEquals(10, grid.getWidth());
        Assertions.assertEquals(10, grid.getHeight());

    }

    @Test
    void revealCellNoCellsRevealed() {
        String idGame = "reveal_cell";
        IdPlayer idPlayer = IdPlayer.PLAYER_1;
        initGame(idGame, idPlayer);

        List<CellDTO> revealedCells = persistenceServiceImpl.getRevealedCells(idGame, idPlayer);

        Assertions.assertTrue(revealedCells.isEmpty());
    }

    @Test
    void revealCell() {

        String idGame = "reveal_cell";
        IdPlayer idPlayer = IdPlayer.PLAYER_1;
        initGame(idGame, idPlayer);

        persistenceServiceImpl.revealCell(idGame, idPlayer, new CoordinateDTO(3, 5));
        List<CellDTO> revealedCells = persistenceServiceImpl.getRevealedCells(idGame, idPlayer);

        List<Cell> cells = cellRepository.findCells(idGame, idPlayer);

        Assertions.assertEquals(1, revealedCells.size());
        Assertions.assertEquals(3, revealedCells.get(0).getX());
        Assertions.assertEquals(5, revealedCells.get(0).getY());
    }

    @Test
    void revealCellOutsideGrid() {

        String idGame = "reveal_cell";
        IdPlayer idPlayer = IdPlayer.PLAYER_1;
        initGame(idGame, idPlayer);

        persistenceServiceImpl.revealCell(idGame, idPlayer, new CoordinateDTO(10, 5));
        List<CellDTO> revealedCells = persistenceServiceImpl.getRevealedCells(idGame, idPlayer);

        List<Cell> cells = cellRepository.findCells(idGame, idPlayer);

        Assertions.assertEquals(0, revealedCells.size());
    }


    @Test
    void setBoatPosition() {

        String idGame = "reveal_cell";
        IdPlayer idPlayer = IdPlayer.PLAYER_1;
        initGame(idGame, idPlayer);

        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.TORPILLEUR);
        boatDTO.setxHead(2);
        boatDTO.setyHead(4);
        boatDTO.setHorizontal(true);
        boatDTO.setDestroyed(false);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition(idGame, idPlayer, boatsDtosInit);

        List<BoatDTO> boats = persistenceServiceImpl.getBoats(idGame, idPlayer);

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


        String idGame = "reveal_cell";
        IdPlayer idPlayer = IdPlayer.PLAYER_1;
        initGame(idGame, idPlayer);

        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.TORPILLEUR);
        boatDTO.setxHead(2);
        boatDTO.setyHead(4);
        boatDTO.setHorizontal(true);
        boatDTO.setDestroyed(false);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition(idGame, idPlayer, boatsDtosInit);

        List<BoatDTO> boats = persistenceServiceImpl.getBoats(idGame, idPlayer);
        Assertions.assertFalse(persistenceServiceImpl.isAllBoatDestroyed(idGame, idPlayer));
    }


    @Test
    void updateStateBoatsHorizontal() {

        String idGame = "reveal_cell";
        IdPlayer idPlayer = IdPlayer.PLAYER_1;
        initGame(idGame, idPlayer);

        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.CROISEUR);
        boatDTO.setxHead(2);
        boatDTO.setyHead(4);
        boatDTO.setHorizontal(true);
        boatDTO.setDestroyed(false);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition(idGame, idPlayer, boatsDtosInit);

        persistenceServiceImpl.revealCell(idGame, idPlayer, new CoordinateDTO(2, 4));
        persistenceServiceImpl.updateStateBoats(idGame, idPlayer);
        Boat croiseur = boatRepository.findBoatByType(idGame, idPlayer, BoatType.CROISEUR);
        Assertions.assertFalse(croiseur.isDestroyed());


        persistenceServiceImpl.revealCell(idGame, idPlayer, new CoordinateDTO(3, 4));
        persistenceServiceImpl.revealCell(idGame, idPlayer, new CoordinateDTO(4, 4));
        persistenceServiceImpl.updateStateBoats(idGame, idPlayer);
        croiseur = boatRepository.findBoatByType(idGame, idPlayer, BoatType.CROISEUR);
        Assertions.assertFalse(croiseur.isDestroyed());


        persistenceServiceImpl.revealCell(idGame, idPlayer, new CoordinateDTO(5, 4));
        persistenceServiceImpl.updateStateBoats(idGame, idPlayer);
        croiseur = boatRepository.findBoatByType(idGame, idPlayer, BoatType.CROISEUR);
        Assertions.assertTrue(croiseur.isDestroyed());


    }


    @Test
    void updateStateBoatsVertical() {
        String idGame = "reveal_cell";
        IdPlayer idPlayer = IdPlayer.PLAYER_1;
        initGame(idGame, idPlayer);

        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.SOUS_MARIN_1);
        boatDTO.setxHead(3);
        boatDTO.setyHead(4);
        boatDTO.setHorizontal(false);
        boatDTO.setDestroyed(false);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition(idGame, idPlayer, boatsDtosInit);

        persistenceServiceImpl.revealCell(idGame, idPlayer, new CoordinateDTO(3, 4));
        persistenceServiceImpl.revealCell(idGame, idPlayer, new CoordinateDTO(3, 5));
        persistenceServiceImpl.updateStateBoats(idGame, idPlayer);
        Boat sousMarin1 = boatRepository.findBoatByType(idGame, idPlayer, BoatType.SOUS_MARIN_1);
        Assertions.assertFalse(sousMarin1.isDestroyed());


        persistenceServiceImpl.revealCell(idGame, idPlayer, new CoordinateDTO(3, 6));
        persistenceServiceImpl.updateStateBoats(idGame, idPlayer);
        sousMarin1 = boatRepository.findBoatByType(idGame, idPlayer, BoatType.SOUS_MARIN_1);
        Assertions.assertTrue(sousMarin1.isDestroyed());

    }

    @Test
    void revealCellsNextToDestroyedBoatVertical() {

        String idGame = "reveal_cell";
        IdPlayer idPlayer = IdPlayer.PLAYER_1;
        initGame(idGame, idPlayer);

        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.SOUS_MARIN_1);
        boatDTO.setxHead(3);
        boatDTO.setyHead(4);
        boatDTO.setHorizontal(false);
        boatDTO.setDestroyed(true);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition(idGame, idPlayer, boatsDtosInit);

        persistenceServiceImpl.revealCellsNextToDestroyedBoat(idGame, idPlayer);

        Assertions.assertTrue(cellRepository.findCellXY(idGame, idPlayer, 3, 4).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY(idGame, idPlayer, 2, 3).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY(idGame, idPlayer, 4, 3).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY(idGame, idPlayer, 2, 7).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY(idGame, idPlayer, 4, 7).isRevealed());

        Assertions.assertFalse(cellRepository.findCellXY(idGame, idPlayer, 5, 7).isRevealed());
        Assertions.assertFalse(cellRepository.findCellXY(idGame, idPlayer, 1, 7).isRevealed());

    }


    @Test
    void revealCellsNextToDestroyedBoatHorizontal() {

        String idGame = "reveal_cell";
        IdPlayer idPlayer = IdPlayer.PLAYER_1;
        initGame(idGame, idPlayer);

        ArrayList<BoatDTO> boatsDtosInit = new ArrayList<BoatDTO>();
        BoatDTO boatDTO = new BoatDTO();
        boatDTO.setBoatType(BoatType.PORTE_AVIONS);
        boatDTO.setxHead(1);
        boatDTO.setyHead(1);
        boatDTO.setHorizontal(true);
        boatDTO.setDestroyed(true);
        boatsDtosInit.add(boatDTO);
        persistenceServiceImpl.setBoatPosition(idGame, idPlayer, boatsDtosInit);

        persistenceServiceImpl.revealCellsNextToDestroyedBoat(idGame, idPlayer);

        Assertions.assertTrue(cellRepository.findCellXY(idGame, idPlayer, 0, 0).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY(idGame, idPlayer, 6, 0).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY(idGame, idPlayer, 0, 2).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY(idGame, idPlayer, 6, 2).isRevealed());
        Assertions.assertTrue(cellRepository.findCellXY(idGame, idPlayer, 2, 1).isRevealed());

        Assertions.assertFalse(cellRepository.findCellXY(idGame, idPlayer, 6, 4).isRevealed());
        Assertions.assertFalse(cellRepository.findCellXY(idGame, idPlayer, 5, 4).isRevealed());

    }


}
