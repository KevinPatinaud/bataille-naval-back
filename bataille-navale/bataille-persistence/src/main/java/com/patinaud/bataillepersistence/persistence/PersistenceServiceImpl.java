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
                player2Cells.add(cell);
            }
        }
        cellRepository.saveAll(player2Cells);

    }


    public ArrayList<CellDTO> getRevealedCells(String idGame, IdPlayer idPlayer) {
        return CellMapper.toDtos(cellRepository.findRevealedCells(idGame, idPlayer));
    }

    public void revealeCell(String idGame, IdPlayer idPlayerTargeted, int x, int y) {

        Cell cell = cellRepository.findCellXY(idGame, idPlayerTargeted, x, y);
        if (cell != null) {
            cell.setRevealed(true);
            cellRepository.save(cell);
        }
    }


    @Override
    public void setBoatPosition(String idGame, IdPlayer idPlayer, ArrayList<BoatDTO> positionBoatOnGrid) {
        boatRepository.saveAll(BoatMapper.toEntities(positionBoatOnGrid, playerRepository.findByGame(idGame, idPlayer)));
    }


    public ArrayList<BoatDTO> getBoats(String idGame, IdPlayer idPlayer) {
        return BoatMapper.toDtos(boatRepository.findBoats(idGame, idPlayer));
    }


    public void updateStateBoats(String idGame, IdPlayer idPlayer) {
        ArrayList<BoatDTO> boats = getBoats(idGame, idPlayer);

        ArrayList<CellDTO> revealedCells = getRevealedCells(idGame, idPlayer);

        for (int i = 0; i < boats.size(); i++) {
            BoatDTO boat = boats.get(i);

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
                destroyBoat(idGame, idPlayer, boat.getBoatType());
            }

        }
    }

    public void destroyBoat(String idGame, IdPlayer idPlayer, BoatType boatType) {
        Boat boat = boatRepository.findBoatByType(idGame, idPlayer, boatType);
        boat.setDestroyed(true);
        boatRepository.save(boat);

    }

    public boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {
        System.out.println("isAllBoatDestroyed");
        System.out.println(boatRepository.findAllNotDestroyedBoats(idGame, idPlayer).isEmpty());

        return boatRepository.findAllNotDestroyedBoats(idGame, idPlayer).isEmpty();
    }

}
