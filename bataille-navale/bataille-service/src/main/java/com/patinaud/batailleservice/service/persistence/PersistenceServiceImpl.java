package com.patinaud.batailleservice.service.persistence;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatPosition;
import com.patinaud.bataillemodel.dto.Cell;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PersistenceServiceImpl implements PersistenceService {

    private ArrayList<Cell> player1RevealedCells = new ArrayList<>();
    private ArrayList<BoatPosition> player1BoatPositions = new ArrayList<>();
    ;
    private ArrayList<Cell> player2RevealedCells = new ArrayList<>();
    private ArrayList<BoatPosition> player2BoatPositions = new ArrayList<>();


    public PersistenceServiceImpl() {

        BoatPosition torpilleur = new BoatPosition();
        torpilleur.setxHead(4);
        torpilleur.setyHead(3);
        torpilleur.setBoatType(BoatType.TORPILLEUR);
        torpilleur.setHorizontal(false);
        player1BoatPositions.add(torpilleur);

        BoatPosition porteAvion = new BoatPosition();
        porteAvion.setxHead(2);
        porteAvion.setyHead(1);
        porteAvion.setBoatType(BoatType.PORTE_AVIONS);
        porteAvion.setHorizontal(true);

        player2BoatPositions.add(porteAvion);


    }

    public ArrayList<Cell> getGrid(String idGame, IdPlayer idplayerToLoad) {
        ArrayList<Cell> grid = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell cell = new Cell();
                cell.setX(x);
                cell.setY(y);
                //   cell.set

                grid.add(cell);
            }
        }

        return grid;
    }


    public ArrayList<Cell> getRevealedCells(String idGame, IdPlayer idPlayer) {
        if (idPlayer.equals(IdPlayer.PLAYER_1)) {
            return player1RevealedCells;
        } else {
            return player2RevealedCells;
        }
    }

    public CellContent revealeCell(String idGame, IdPlayer idPlayerTargeted, int x, int y) {

        Cell cell = new Cell();
        cell.setX(x);
        cell.setY(y);
        cell.setRevealed(true);
        cell.setCellContent(getCellContent(idGame, idPlayerTargeted, x, y));

        if (idPlayerTargeted.equals(IdPlayer.PLAYER_1)) {
            player1RevealedCells.add(cell);
        } else {
            player2RevealedCells.add(cell);
        }

        return CellContent.BOAT;
    }


    public ArrayList<BoatPosition> getBoats(String idGame, IdPlayer idPlayer) {
        return (idPlayer.equals(IdPlayer.PLAYER_1) ? player1BoatPositions : player2BoatPositions);
    }

    public CellContent getCellContent(String idGame, IdPlayer idPlayer, int x, int y) {
        ArrayList<BoatPosition> boats = getBoats(idGame, idPlayer);

        for (int i = 0; i < boats.size(); i++) {
            BoatPosition boat = boats.get(i);

            int xBoat = boat.getxHead();
            int yBoat = boat.getyHead();

            for (int inc = 0; inc < boat.getBoatType().getSize(); inc++) {
                if (x == xBoat + (boat.isHorizontal() ? inc : 0) && y == yBoat + (!boat.isHorizontal() ? inc : 0)) {
                    return CellContent.BOAT;
                }
            }

        }

        return CellContent.NOTHING;
    }

    public void updateStateBoats(String idGame, IdPlayer idPlayer) {
        ArrayList<BoatPosition> boats = getBoats(idGame, idPlayer);

        ArrayList<Cell> revealedCells = getRevealedCells(idGame, idPlayer);

        for (int i = 0; i < boats.size(); i++) {
            BoatPosition boat = boats.get(i);

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
                destroyBoat(idGame, idPlayer, boat);
            }

        }
    }

    public void destroyBoat(String idGame, IdPlayer idPlayer, BoatPosition boat) {
        player2BoatPositions.get(0).setDestroyed(true);
    }

    public boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {

        ArrayList<BoatPosition> boats = getBoats(idGame, idPlayer);


        System.out.println(boats.get(0).isDestroyed());

        System.out.println(boats.stream().filter(boat -> !boat.isDestroyed()).toList().size());
        System.out.println(boats.stream().filter(boat -> !boat.isDestroyed()).toList().isEmpty());

        return boats.stream().filter(boat -> !boat.isDestroyed()).toList().isEmpty();
    }

}
