package com.patinaud.bataillepersistence.persistence;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.GameDTO;
import com.patinaud.bataillepersistence.dao.BoatRepository;
import com.patinaud.bataillepersistence.dao.CellRepository;
import com.patinaud.bataillepersistence.dao.GameRepository;
import com.patinaud.bataillepersistence.dao.PlayerRepository;
import com.patinaud.bataillepersistence.entity.Boat;
import com.patinaud.bataillepersistence.entity.Cell;
import com.patinaud.bataillepersistence.entity.Game;
import com.patinaud.bataillepersistence.entity.Player;
import com.patinaud.bataillepersistence.mapper.BoatMapper;
import com.patinaud.bataillepersistence.mapper.CellMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PersistenceServiceImpl implements PersistenceService {


    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CellRepository cellRepository;


    @Autowired
    BoatRepository boatRepository;


    public void initializeGame(String idGame) {

        Game game = new Game();
        game.setIdGame(idGame);
        game.setIdPlayerTurn(IdPlayer.PLAYER_1);
        gameRepository.save(game);

        Player player1 = new Player();
        player1.setIdPlayer(IdPlayer.PLAYER_1);
        player1.setGame(game);
        player1.setIA(false);
        playerRepository.save(player1);


        ArrayList<Cell> player1Cells = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell cell = new Cell();
                cell.setX(x);
                cell.setY(y);
                cell.setPlayer(player1);
                cell.setRevealed(false);
                cell.setOccupied(false);
                player1Cells.add(cell);
            }
        }
        cellRepository.saveAll(player1Cells);

        Player player2 = new Player();
        player2.setIdPlayer(IdPlayer.PLAYER_2);
        player2.setGame(game);
        player2.setIA(true);
        playerRepository.save(player2);


        ArrayList<Cell> player2Cells = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell cell = new Cell();
                cell.setX(x);
                cell.setY(y);
                cell.setPlayer(player2);
                cell.setRevealed(false);
                cell.setOccupied(false);
                player2Cells.add(cell);
            }
        }
        cellRepository.saveAll(player2Cells);

    }

    @Override
    public ArrayList<CellDTO> getRevealedCells(String idGame, IdPlayer idPlayer) {
        return CellMapper.toDtos(cellRepository.findRevealedCells(idGame, idPlayer));
    }


    @Override
    public void revealCell(String idGame, IdPlayer idPlayer, int x, int y) {

        Cell cell = cellRepository.findCellXY(idGame, idPlayer, x, y);
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
    public void setBoatPosition(String idGame, IdPlayer idPlayer, ArrayList<BoatDTO> positionBoatOnGrid) {
        ArrayList<Boat> boats = BoatMapper.toEntities(positionBoatOnGrid, playerRepository.findByGame(idGame, idPlayer));

        boatRepository.saveAll(boats);

        for (int iBoat = 0; iBoat < boats.size(); iBoat++) {
            int xMin = boats.get(iBoat).getxHead();
            int yMin = boats.get(iBoat).getyHead();
            int xMax = boats.get(iBoat).getxHead() + (boats.get(iBoat).isHorizontal() ? boats.get(iBoat).getBoatType().getSize() - 1 : 0);
            int yMax = boats.get(iBoat).getyHead() + (!boats.get(iBoat).isHorizontal() ? boats.get(iBoat).getBoatType().getSize() - 1 : 0);

            ArrayList<Cell> cells = cellRepository.findAreaCells(idGame, idPlayer, xMin, xMax, yMin, yMax);

            if (idPlayer.equals(IdPlayer.PLAYER_1)) {
                System.out.println("..........................................");
                System.out.println("setBoatPosition");
                System.out.println("boats.get(iBoat).getxHead() : " + boats.get(iBoat).getxHead());
                System.out.println("boats.get(iBoat).getyHead() : " + boats.get(iBoat).getyHead());
                System.out.println("boats.get(iBoat).getBoatType().getName() : " + boats.get(iBoat).getBoatType().getName());
                System.out.println("boats.get(iBoat).getBoatType().getSize() : " + boats.get(iBoat).getBoatType().getSize());
                System.out.println("boats.get(iBoat).isHorizontal() : " + boats.get(iBoat).isHorizontal());
                System.out.println("xMin : " + xMin);
                System.out.println("yMin : " + yMin);
                System.out.println("xMax : " + xMax);
                System.out.println("yMax : " + yMax);

            }

            for (int iCell = 0; iCell < cells.size(); iCell++) {
                cells.get(iCell).setOccupied(true);
                if (idPlayer.equals(IdPlayer.PLAYER_1)) {
                    System.out.println(" ");
                    System.out.println("x cell  : " + cells.get(iCell).getX());
                    System.out.println("y cell  : " + cells.get(iCell).getY());
                    System.out.println(" ");
                }
            }

            cellRepository.saveAll(cells);
        }

    }

    @Override
    public ArrayList<BoatDTO> getBoats(String idGame, IdPlayer idPlayer) {
        return BoatMapper.toDtos(boatRepository.findBoats(idGame, idPlayer));
    }

    @Override
    public void updateStateBoats(String idGame, IdPlayer idPlayer) {
        ArrayList<Boat> boats = boatRepository.findBoats(idGame, idPlayer);

        ArrayList<Cell> revealedCells = cellRepository.findRevealedCells(idGame, idPlayer);

        for (int i = 0; i < boats.size(); i++) {
            Boat boat = boats.get(i);

            int xBoatHead = boat.getxHead();
            int yBoatHead = boat.getyHead();

            boolean boatDestroyed = true;

            for (int inc = 0; inc < boat.getBoatType().getSize(); inc++) {
                boolean boatCellTouched = false;

                for (int iRevCell = 0; iRevCell < revealedCells.size(); iRevCell++) {

                    if (revealedCells.get(iRevCell).getX() == xBoatHead + (boat.isHorizontal() ? inc : 0) && revealedCells.get(iRevCell).getY() == yBoatHead + (!boat.isHorizontal() ? inc : 0)) {
                        boatCellTouched = true;
                    }
                }
                if (!boatCellTouched) {
                    boatDestroyed = false;
                }
            }

            if (boatDestroyed) {
                boat.setDestroyed(true);
                boatRepository.save(boat);
            }

        }
    }

    @Override
    public boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {
        return boatRepository.findBoatsByDestroyedState(idGame, idPlayer, false).isEmpty();
    }


}
