package com.patinaud.batailleplayer.ia;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class IaPlayerService_impl implements IaPlayerService {
    @Override
    public ArrayList<BoatDTO> positionBoatOnGrid(List<BoatType> boatsToPositions, int widthGrid, int heigthGrid) {

        ArrayList<BoatDTO> boatsPositions = new ArrayList<>();

        for (int i = 0; i < boatsToPositions.size(); i++) {

            boatsPositions.add(findAPositionForTheBoat(boatsToPositions.get(i), boatsPositions, widthGrid, heigthGrid));
        }

        return boatsPositions;
    }


    public BoatDTO findAPositionForTheBoat(BoatType boatType, List<BoatDTO> alreadyPositionnedBoats, int widthGrid, int heigthGrid) {
        Random random = new Random();

        int xHead = random.nextInt(widthGrid);
        int yHead = random.nextInt(heigthGrid);
        boolean isHorizontal = random.nextInt(2) == 0;

        if (!theBoatIsPositionnedInsideTheGrid(boatType, xHead, yHead, isHorizontal, widthGrid, heigthGrid)) {
            return findAPositionForTheBoat(boatType, alreadyPositionnedBoats, widthGrid, heigthGrid);
        }


        if (!thePositionIsFree(boatType, alreadyPositionnedBoats, xHead, yHead, isHorizontal)) {
            return findAPositionForTheBoat(boatType, alreadyPositionnedBoats, widthGrid, heigthGrid);
        }

        BoatDTO boat = new BoatDTO();
        boat.setxHead(xHead);
        boat.setyHead(yHead);
        boat.setHorizontal(isHorizontal);
        boat.setDestroyed(false);
        boat.setBoatType(boatType);

        return boat;
    }

    public boolean theBoatIsPositionnedInsideTheGrid(BoatType boatType, int xHead, int yHead, boolean isHorizontal, int widthGrid, int heigthGrid) {

        if (xHead >= 0 && xHead < widthGrid && yHead >= 0 && yHead < heigthGrid) {
            if (isHorizontal && xHead + boatType.getSize() < widthGrid) {
                return true;
            }
            if (!isHorizontal && yHead + boatType.getSize() < heigthGrid) {
                return true;
            }
        }
        return false;
    }

    public boolean thePositionIsFree(BoatType boatType, List<BoatDTO> alreadyPositionnedBoats, int xHead, int yHead, boolean isHorizontal) {
        // Vérifie que toutes les cellules occupées par le bateau sont libre
        boolean allCellsAreFree = true;
        for (int i = 0; i < boatType.getSize(); i++) {
            int x = xHead + (isHorizontal ? i : 0);
            int y = yHead + (!isHorizontal ? i : 0);

            if (cellContainsABoat(alreadyPositionnedBoats, x, y)) {
                allCellsAreFree = false;
            }
        }

        return allCellsAreFree;
    }

    public boolean cellContainsABoat(List<BoatDTO> boats, int xCell, int yCell) {
        if (boats == null) return false;

        boolean cellContainsABoat = false;

        for (int i = 0; i < boats.size(); i++) {
            if (boatOccupiesTheCell(boats.get(i), xCell, yCell)) {
                cellContainsABoat = true;
            }
        }

        return cellContainsABoat;
    }

    public boolean boatOccupiesTheCell(BoatDTO boat, int xCell, int yCell) {
        int xMin = boat.getxHead() - 1;
        int yMin = boat.getyHead() - 1;
        int xMax = boat.getxHead() + (boat.isHorizontal() ? boat.getBoatType().getSize() : 1);
        int yMax = boat.getyHead() + (!boat.isHorizontal() ? boat.getBoatType().getSize() : 1);

        return xCell >= xMin && xCell <= xMax && yCell >= yMin && yCell <= yMax;
    }


    public CoordinateDTO iaAttack(ArrayList<CellDTO> cellsRevealed, int widthGrid, int heigthGrid) {

        CoordinateDTO coordinateToAttack = calculBestCoordToAttack(cellsRevealed, widthGrid, heigthGrid);
        if (coordinateToAttack == null) {
            coordinateToAttack = randomlyTargetACoveredCell(cellsRevealed, widthGrid, heigthGrid);
        }

        return coordinateToAttack;
    }


    public CoordinateDTO calculBestCoordToAttack(ArrayList<CellDTO> cellsRevealed, int widthGrid, int heigthGrid) {
        for (int i = 0; i < cellsRevealed.size(); i++) {
            CellDTO cell = cellsRevealed.get(i);

            if (cell.isOccupied()) {
                CoordinateDTO coordinateToAttack = calculBestCoordToAttackFromBoat(cellsRevealed, cell.getX(), cell.getY(), widthGrid, heigthGrid);

                if (coordinateToAttack != null) {
                    return coordinateToAttack;
                }

                coordinateToAttack = calculBestCoordToAttackFromCloud(cellsRevealed, cell.getX(), cell.getY(), widthGrid, heigthGrid);

                if (coordinateToAttack != null) {
                    return coordinateToAttack;
                }

            }

        }
        return null;
    }


    public CoordinateDTO randomlyTargetACoveredCell(ArrayList<CellDTO> cellsRevealed, int widthGrid, int heigthGrid) {
        Random random = new Random();

        int xRand = random.nextInt(widthGrid);
        int yRand = random.nextInt(widthGrid);
        if (cellsRevealed != null) {
            while (getCellByCoordinate(cellsRevealed, xRand, yRand) != null) {
                xRand = random.nextInt(widthGrid);
                yRand = random.nextInt(widthGrid);
            }
        }

        return new CoordinateDTO(xRand, yRand);
    }


    public int countNumberOfCellBoatRight(ArrayList<CellDTO> cellsRevealed, int x, int y, int widthGrid) {
        int inc = 0;
        while (x + inc < widthGrid
                && getCellByCoordinate(cellsRevealed, x + inc, y) != null
                && getCellByCoordinate(cellsRevealed, x + inc, y).isOccupied()) {
            inc++;
        }
        return inc;
    }

    public int countNumberOfCellBoatLeft(ArrayList<CellDTO> cellsRevealed, int x, int y) {
        int inc = 0;
        while (x - inc >= 0
                && getCellByCoordinate(cellsRevealed, x - inc, y) != null
                && getCellByCoordinate(cellsRevealed, x - inc, y).isOccupied()) {
            inc++;
        }
        return inc;
    }


    public int countNumberOfCellBoatBottom(ArrayList<CellDTO> cellsRevealed, int x, int y, int heigthGrid) {
        int inc = 0;
        while (y + inc < heigthGrid
                && getCellByCoordinate(cellsRevealed, x, y + inc) != null
                && getCellByCoordinate(cellsRevealed, x, y + inc).isOccupied()) {
            inc++;
        }
        return inc;
    }


    public int countNumberOfCellBoatTop(ArrayList<CellDTO> cellsRevealed, int x, int y) {
        int inc = 0;
        while (y - inc >= 0
                && getCellByCoordinate(cellsRevealed, x, y - inc) != null
                && getCellByCoordinate(cellsRevealed, x, y - inc).isOccupied()) {
            inc++;
        }
        return inc;
    }

    public CoordinateDTO calculBestCoordToAttackFromBoat(ArrayList<CellDTO> cellsRevealed, int x, int y, int widthGrid, int heigthGrid) {

        int nmbBoatCellRight = countNumberOfCellBoatRight(cellsRevealed, x, y, widthGrid);
        if (nmbBoatCellRight > 1 && x + nmbBoatCellRight < widthGrid && getCellByCoordinate(cellsRevealed, x + nmbBoatCellRight, y) == null) {
            return new CoordinateDTO(x + nmbBoatCellRight, y);
        }

        int nmbBoatCellLeft = countNumberOfCellBoatLeft(cellsRevealed, x, y);
        if (nmbBoatCellLeft > 1 && x - nmbBoatCellLeft >= 0 && getCellByCoordinate(cellsRevealed, x - nmbBoatCellLeft, y) == null) {
            return new CoordinateDTO(x - nmbBoatCellLeft, y);
        }


        int nmbBoatCellBottom = countNumberOfCellBoatBottom(cellsRevealed, x, y, heigthGrid);
        if (nmbBoatCellBottom > 1 && y + nmbBoatCellBottom < heigthGrid && getCellByCoordinate(cellsRevealed, x, y + nmbBoatCellBottom) == null) {
            return new CoordinateDTO(x, y + nmbBoatCellBottom);
        }


        int nmbBoatCellTop = countNumberOfCellBoatTop(cellsRevealed, x, y);
        if (nmbBoatCellTop > 1 && y - nmbBoatCellTop >= 0 && getCellByCoordinate(cellsRevealed, x, y - nmbBoatCellTop) == null) {
            return new CoordinateDTO(x, y - nmbBoatCellTop);
        }

        return null;
    }


    public CoordinateDTO calculBestCoordToAttackFromCloud(ArrayList<CellDTO> cellsRevealed, int x, int y, int widthGrid, int heigthGrid) {

        // on n'a pas encore vu de bateau, alors on élimine les nuages

        if (x + 1 < widthGrid && getCellByCoordinate(cellsRevealed, x + 1, y) == null) {
            return new CoordinateDTO(x + 1, y);
        }

        if (x - 1 >= 0 && getCellByCoordinate(cellsRevealed, x - 1, y) == null) {
            return new CoordinateDTO(x - 1, y);
        }

        if (y + 1 < heigthGrid && getCellByCoordinate(cellsRevealed, x, y + 1) == null) {
            return new CoordinateDTO(x, y + 1);
        }

        if (y - 1 >= 0 && getCellByCoordinate(cellsRevealed, x, y - 1) == null) {
            return new CoordinateDTO(x, y - 1);
        }


        return null;
    }

    public CellDTO getCellByCoordinate(ArrayList<CellDTO> cells, int x, int y) {
        Optional<CellDTO> result = cells.stream().filter(c -> c.getX() == x && c.getY() == y).findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

}
