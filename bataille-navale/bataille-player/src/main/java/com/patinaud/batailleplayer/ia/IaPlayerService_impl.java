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

        Optional<CoordinateDTO> coordinateToAttack = cellsRevealed.stream().filter(cell -> cell.isOccupied())
                .map((cell) -> calculBestCoordToAttackFromBoat(cellsRevealed, cell.getX(), cell.getY(), widthGrid, heigthGrid))
                .filter(cell -> cell != null)
                .findFirst();

        if (coordinateToAttack.isPresent()) {
            return coordinateToAttack.get();
        }


        coordinateToAttack = cellsRevealed.stream().filter(cell -> cell.isOccupied())
                .map((cell) -> calculBestCoordToAttackFromCloud(cellsRevealed, cell.getX(), cell.getY(), widthGrid, heigthGrid))
                .filter(cell -> cell != null)
                .findFirst();

        if (coordinateToAttack.isPresent()) {
            return coordinateToAttack.get();
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


    public int countNumberOfCellBoat(ArrayList<CellDTO> cellsRevealed, int xInit, int yInit, int evolveX, int evolveY, int widthGrid, int heigthGrid) {
        int inc = 0;
        int x = xInit + evolveX * inc;
        int y = yInit + evolveY * inc;

        while (
                x >= 0 && x < widthGrid &&
                        y >= 0 && y < heigthGrid &&
                        getCellByCoordinate(cellsRevealed, x, y) != null &&
                        getCellByCoordinate(cellsRevealed, x, y).isOccupied()) {
            inc++;
            x = xInit + evolveX * inc;
            y = yInit + evolveY * inc;
        }
        return inc;
    }


    public CoordinateDTO calculBestCoordToAttackFromBoat(ArrayList<CellDTO> cellsRevealed, int x, int y, int widthGrid, int heigthGrid) {


        int nmbBoatCellRight = countNumberOfCellBoat(cellsRevealed, x, y, 1, 0, widthGrid, heigthGrid);
        int nmbBoatCellLeft = countNumberOfCellBoat(cellsRevealed, x, y, -1, 0, widthGrid, heigthGrid);
        int nmbBoatCellBottom = countNumberOfCellBoat(cellsRevealed, x, y, 0, 1, widthGrid, heigthGrid);
        int nmbBoatCellTop = countNumberOfCellBoat(cellsRevealed, x, y, 0, -1, widthGrid, heigthGrid);


        if (nmbBoatCellRight > 1) {
            if (x + nmbBoatCellRight < widthGrid && getCellByCoordinate(cellsRevealed, x + nmbBoatCellRight, y) == null) {
                return new CoordinateDTO(x + nmbBoatCellRight, y);
            }
            if (x - nmbBoatCellLeft >= 0 && getCellByCoordinate(cellsRevealed, x - nmbBoatCellLeft, y) == null) {
                return new CoordinateDTO(x - nmbBoatCellLeft, y);
            }
        }

        if (nmbBoatCellLeft > 1) {
            if (x - nmbBoatCellLeft >= 0 && getCellByCoordinate(cellsRevealed, x - nmbBoatCellLeft, y) == null) {
                return new CoordinateDTO(x - nmbBoatCellLeft, y);
            }
            if (x + nmbBoatCellRight < widthGrid && getCellByCoordinate(cellsRevealed, x + nmbBoatCellRight, y) == null) {
                return new CoordinateDTO(x + nmbBoatCellRight, y);
            }
        }


        if (nmbBoatCellBottom > 1) {
            if (y + nmbBoatCellBottom < heigthGrid && getCellByCoordinate(cellsRevealed, x, y + nmbBoatCellBottom) == null) {
                return new CoordinateDTO(x, y + nmbBoatCellBottom);
            }
            if (y - nmbBoatCellTop > 0 && getCellByCoordinate(cellsRevealed, x, y - nmbBoatCellTop) == null) {
                return new CoordinateDTO(x, y - nmbBoatCellTop);
            }
        }


        if (nmbBoatCellTop > 1) {
            if (y - nmbBoatCellTop >= 0 && getCellByCoordinate(cellsRevealed, x, y - nmbBoatCellTop) == null) {
                return new CoordinateDTO(x, y - nmbBoatCellTop);
            }
            if (y + nmbBoatCellBottom < heigthGrid && getCellByCoordinate(cellsRevealed, x, y + nmbBoatCellBottom) == null) {
                return new CoordinateDTO(x, y + nmbBoatCellBottom);
            }
        }

        return null;
    }


    public CoordinateDTO calculBestCoordToAttackFromCloud(ArrayList<CellDTO> cellsRevealed, int x, int y, int widthGrid, int heightGrid) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (newX >= 0 && newX < widthGrid && newY >= 0 && newY < heightGrid && getCellByCoordinate(cellsRevealed, newX, newY) == null) {
                return new CoordinateDTO(newX, newY);
            }
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
