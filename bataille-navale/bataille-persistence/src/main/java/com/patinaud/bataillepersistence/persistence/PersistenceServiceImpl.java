package com.patinaud.bataillepersistence.persistence;

import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillepersistence.entity.GameEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PersistenceServiceImpl implements PersistenceService {


    private ArrayList<CellDTO> player1RevealedCells = new ArrayList<>();
    private ArrayList<BoatDTO> player1Boats = new ArrayList<>();
    ;
    private ArrayList<CellDTO> player2RevealedCells = new ArrayList<>();
    private ArrayList<BoatDTO> player2Boats = new ArrayList<>();


    public void initializeGame(String idGame) {
        player1RevealedCells = new ArrayList<>();
        player1Boats = new ArrayList<>();
        player2RevealedCells = new ArrayList<>();
        player2Boats = new ArrayList<>();

        GameEntity game = new GameEntity();
        // game.setIdGame(idGame);

    }


    public ArrayList<CellDTO> getGrid(String idGame, IdPlayer idplayerToLoad) {
        ArrayList<CellDTO> grid = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                CellDTO cell = new CellDTO();
                cell.setX(x);
                cell.setY(y);
                //   cell.set

                grid.add(cell);
            }
        }

        return grid;
    }


    public ArrayList<CellDTO> getRevealedCells(String idGame, IdPlayer idPlayer) {
        if (idPlayer.equals(IdPlayer.PLAYER_1)) {
            return player1RevealedCells;
        } else {
            return player2RevealedCells;
        }
    }

    public CellContent revealeCell(String idGame, IdPlayer idPlayerTargeted, int x, int y) {

        CellDTO cell = new CellDTO();
        cell.setX(x);
        cell.setY(y);
        cell.setRevealed(true);
        //   cell.setCellContent(getCellContent(idGame, idPlayerTargeted, x, y));

        if (idPlayerTargeted.equals(IdPlayer.PLAYER_1)) {
            player1RevealedCells.add(cell);
        } else {
            player2RevealedCells.add(cell);
        }

        return CellContent.BOAT;
    }


    public ArrayList<BoatDTO> getBoats(String idGame, IdPlayer idPlayer) {
        return (idPlayer.equals(IdPlayer.PLAYER_1) ? player1Boats : player2Boats);
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
                destroyBoat(idGame, idPlayer, boat);
            }

        }
    }

    @Override
    public void setBoatPosition(String idGame, IdPlayer idPlayer, ArrayList<BoatDTO> positionBoatOnGrid) {
        if (idPlayer.equals(IdPlayer.PLAYER_2)) {
            player2Boats = positionBoatOnGrid;
        }
    }

    public void destroyBoat(String idGame, IdPlayer idPlayer, BoatDTO boat) {

        for (int i = 0; i < player2Boats.size(); i++) {
            if (player2Boats.get(i).getBoatType().equals(boat.getBoatType())) {
                player2Boats.get(i).setDestroyed(true);
            }
        }

    }

    public boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {

        ArrayList<BoatDTO> boats = getBoats(idGame, idPlayer);


        System.out.println("PersistenceService . isAllBoatDestroyed");
        for (int i = 0; i < boats.size(); i++) {
            System.out.println("index " + i + " is destroyed : " + boats.get(i).isDestroyed());
        }

        System.out.println(boats.stream().filter(boat -> !boat.isDestroyed()).toList().size());
        System.out.println(boats.stream().filter(boat -> !boat.isDestroyed()).toList().isEmpty());

        return boats.stream().filter(boat -> !boat.isDestroyed()).toList().isEmpty();
    }

}
