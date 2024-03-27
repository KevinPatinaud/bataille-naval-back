package com.patinaud.bataillepersistence.persistence;


import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.*;
import com.patinaud.bataillepersistence.dao.BoatRepository;
import com.patinaud.bataillepersistence.dao.CellRepository;
import com.patinaud.bataillepersistence.dao.GameRepository;
import com.patinaud.bataillepersistence.dao.PlayerRepository;
import com.patinaud.bataillepersistence.entity.*;
import com.patinaud.bataillepersistence.mapper.BoatMapper;
import com.patinaud.bataillepersistence.mapper.CellMapper;
import com.patinaud.bataillepersistence.mapper.GameMapper;
import com.patinaud.bataillepersistence.mapper.GridMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersistenceServiceImpl implements PersistenceService {

    GameRepository gameRepository;

    PlayerRepository playerRepository;

    CellRepository cellRepository;

    BoatRepository boatRepository;

    @Autowired
    public PersistenceServiceImpl(GameRepository gameRepository, PlayerRepository playerRepository, CellRepository cellRepository, BoatRepository boatRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.cellRepository = cellRepository;
        this.boatRepository = boatRepository;
    }


    public void saveGrid(String idGame, IdPlayer idPlayer, GridDTO grid) {
        Player player = playerRepository.findByGame(idGame, idPlayer);
        List<Cell> playerCells = GridMapper.toCellsEntities(grid, player);
        cellRepository.saveAll(playerCells);
    }

    public void saveGame(GameDTO gameDto) {
        gameRepository.save(GameMapper.toEntity(gameDto));
    }


    public void savePlayer(PlayerDTO playerDto) {
        playerRepository.save(PlayerMapper.toEntity(playerDto));
    }

    @Override
    public List<CellDTO> getRevealedCells(String idGame, IdPlayer idPlayer) {
        return CellMapper.toDtos(cellRepository.findRevealedCells(idGame, idPlayer));
    }


    @Override
    public GridDTO getGrid(String idGame, IdPlayer idPlayer) {
        return GridMapper.toDto(cellRepository.findCells(idGame, idPlayer));
    }

    @Override
    public void revealCell(String idGame, IdPlayer idPlayer, CoordinateDTO coordinate) {

        Cell cell = cellRepository.findCellXY(idGame, idPlayer, coordinate.getX(), coordinate.getY());
        if (cell != null) {
            cell.setRevealed(true);
            cellRepository.save(cell);
        }
    }

    @Override
    public void revealCellsNextToDestroyedBoat(String idGame, IdPlayer idPlayer) {
        ArrayList<Boat> boats = boatRepository.findBoatsByDestroyedState(idGame, idPlayer, true);

        for (int iBoat = 0; iBoat < boats.size(); iBoat++) {
            int xMin = boats.get(iBoat).getxHead() - 1;
            int yMin = boats.get(iBoat).getyHead() - 1;
            int xMax = boats.get(iBoat).getxHead() + (boats.get(iBoat).isHorizontal() ? boats.get(iBoat).getBoatType().getSize() : 1);
            int yMax = boats.get(iBoat).getyHead() + (!boats.get(iBoat).isHorizontal() ? boats.get(iBoat).getBoatType().getSize() : 1);

            ArrayList<Cell> cells = cellRepository.findAreaCells(idGame, idPlayer, xMin, xMax, yMin, yMax);

            for (int iCell = 0; iCell < cells.size(); iCell++) {
                cells.get(iCell).setRevealed(true);
            }

            cellRepository.saveAll(cells);
        }
    }

    @Override
    public void setBoatPosition(String idGame, IdPlayer idPlayer, List<BoatDTO> positionBoatOnGrid) {
        List<Boat> boats = BoatMapper.toEntities(positionBoatOnGrid, playerRepository.findByGame(idGame, idPlayer));

        boatRepository.saveAll(boats);

        for (int iBoat = 0; iBoat < boats.size(); iBoat++) {
            int xMin = boats.get(iBoat).getxHead();
            int yMin = boats.get(iBoat).getyHead();
            int xMax = boats.get(iBoat).getxHead() + (boats.get(iBoat).isHorizontal() ? boats.get(iBoat).getBoatType().getSize() - 1 : 0);
            int yMax = boats.get(iBoat).getyHead() + (!boats.get(iBoat).isHorizontal() ? boats.get(iBoat).getBoatType().getSize() - 1 : 0);

            ArrayList<Cell> cells = cellRepository.findAreaCells(idGame, idPlayer, xMin, xMax, yMin, yMax);

            for (int iCell = 0; iCell < cells.size(); iCell++) {
                cells.get(iCell).setOccupied(true);
            }

            cellRepository.saveAll(cells);
        }

    }

    @Override
    public List<BoatDTO> getBoats(String idGame, IdPlayer idPlayer) {
        return BoatMapper.toDtos(boatRepository.findBoats(idGame, idPlayer));
    }

    @Override
    public void updateStateBoats(String idGame, IdPlayer idPlayer) {
        List<Boat> boats = boatRepository.findBoats(idGame, idPlayer);

        List<Cell> revealedCells = cellRepository.findRevealedCells(idGame, idPlayer);

        for (int i = 0; i < boats.size(); i++) {
            Boat boat = boats.get(i);

            if (isBoatDestroyed(boat, revealedCells)) {
                boat.setDestroyed(true);
                boatRepository.save(boat);
            }

        }
    }

    public boolean isBoatDestroyed(Boat boat, List<Cell> revealedCells) {

        int xBoatHead = boat.getxHead();
        int yBoatHead = boat.getyHead();

        for (int inc = 0; inc < boat.getBoatType().getSize(); inc++) {
            boolean boatCellTouched = false;

            for (int iRevCell = 0; iRevCell < revealedCells.size(); iRevCell++) {

                if (revealedCells.get(iRevCell).getX() == xBoatHead + (boat.isHorizontal() ? inc : 0) && revealedCells.get(iRevCell).getY() == yBoatHead + (!boat.isHorizontal() ? inc : 0)) {
                    boatCellTouched = true;
                }
            }
            if (!boatCellTouched) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {
        return boatRepository.findBoatsByDestroyedState(idGame, idPlayer, false).isEmpty();
    }


}
