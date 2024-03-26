package com.patinaud.batailleplayer.ia;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IaPlayerServiceImpl implements IaPlayerService {

    private final Random random = new Random();

    @Override
    public ArrayList<BoatDTO> positionBoatOnGrid(List<BoatType> boatsToPositions, GridDTO grid) {

        ArrayList<BoatDTO> boatsPositions = new ArrayList<>();

        for (int i = 0; i < boatsToPositions.size(); i++) {

            boatsPositions.add(findAPositionForTheBoat(boatsToPositions.get(i), boatsPositions, grid));
        }

        return boatsPositions;
    }


    public BoatDTO findAPositionForTheBoat(BoatType boatType, List<BoatDTO> alreadyPositionnedBoats, GridDTO grid) {
        int xHead = random.nextInt(grid.getWidth());
        int yHead = random.nextInt(grid.getHeight());
        boolean isHorizontal = random.nextInt(2) == 0;

        BoatDTO boatToPosition = new BoatDTO();
        boatToPosition.setxHead(xHead);
        boatToPosition.setyHead(yHead);
        boatToPosition.setHorizontal(isHorizontal);
        boatToPosition.setDestroyed(false);
        boatToPosition.setBoatType(boatType);

        if (!theBoatSizeCanEnterInTheGrid(boatToPosition, grid)) {
            return findAPositionForTheBoat(boatType, alreadyPositionnedBoats, grid);
        }


        if (!thePositionIsFree(boatToPosition, alreadyPositionnedBoats)) {
            return findAPositionForTheBoat(boatType, alreadyPositionnedBoats, grid);
        }

        return boatToPosition;
    }

    public boolean theBoatSizeCanEnterInTheGrid(BoatDTO boat, GridDTO grid) {

        if (boat.getxHead() >= 0 && boat.getxHead() < grid.getWidth() && boat.getyHead() >= 0 && boat.getyHead() < grid.getHeight()) {
            if (boat.isHorizontal() && boat.getxHead() + boat.getBoatType().getSize() < grid.getWidth()) {
                return true;
            }
            if (!boat.isHorizontal() && boat.getyHead() + boat.getBoatType().getSize() < grid.getHeight()) {
                return true;
            }
        }
        return false;
    }

    public boolean thePositionIsFree(BoatDTO boat, List<BoatDTO> alreadyPositionedBoats) {

        for (int i = 0; i < boat.getBoatType().getSize(); i++) {
            int x = boat.getxHead() + (boat.isHorizontal() ? i : 0);
            int y = boat.getyHead() + (!boat.isHorizontal() ? i : 0);

            if (cellContainsABoat(alreadyPositionedBoats, x, y)) {
                return false;
            }
        }

        return true;
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

        Optional<CoordinateDTO> coordinateToAttack = cellsRevealed.stream().filter(CellDTO::isOccupied)
                .map(cell -> calculBestCoordToAttackFromBoat(cellsRevealed, cell.getX(), cell.getY(), widthGrid, heigthGrid))
                .filter(Objects::nonNull)
                .findFirst();

        if (coordinateToAttack.isPresent()) {
            return coordinateToAttack.get();
        }


        coordinateToAttack = cellsRevealed.stream().filter(CellDTO::isOccupied)
                .map(cell -> calculBestCoordToAttackFromCloud(cellsRevealed, cell.getX(), cell.getY(), widthGrid, heigthGrid))
                .filter(Objects::nonNull)
                .findFirst();

        if (coordinateToAttack.isPresent()) {
            return coordinateToAttack.get();
        }

        return null;
    }


    public CoordinateDTO randomlyTargetACoveredCell(ArrayList<CellDTO> cellsRevealed, int widthGrid, int heightGrid) {

        ArrayList<PonderationCell> ponderations = new ArrayList<>();
        int totalWeight = 0;

        for (int iX = 0; iX < widthGrid; iX++) {
            for (int iY = 0; iY < heightGrid; iY++) {
                if (getCellByCoordinate(cellsRevealed, iX, iY) == null) {

                    int weight = calculateWeight(cellsRevealed, iX, iY, widthGrid, heightGrid);

                    if (weight > 1) {
                        totalWeight = totalWeight + weight;
                        ponderations.add(new PonderationCell(weight, iX, iY));
                    }
                }
            }
        }


        int indexWeightCell = random.nextInt(totalWeight);
        int recoveryWeight = 0;
        for (int i = 0; i < ponderations.size(); i++) {
            recoveryWeight = recoveryWeight + ponderations.get(i).getWeight();

            if (recoveryWeight > indexWeightCell) {
                return new CoordinateDTO(ponderations.get(i).getX(), ponderations.get(i).getY());
            }
        }

        return null;
    }


    private int calculateWeight(ArrayList<CellDTO> cellsRevealed, int x, int y, int widthGrid, int heightGrid) {
        int weight = 1;
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : directions) {
            if (isInGrid(x + dir[0], y + dir[1], widthGrid, heightGrid) && getCellByCoordinate(cellsRevealed, x + dir[0], y + dir[1]) == null) {
                weight *= 2;
            }
        }
        return weight;
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


    public boolean isInGrid(int x, int y, int widthGrid, int heigthGrid) {
        return x >= 0 && x < widthGrid && y >= 0 && y < heigthGrid;
    }


    public CoordinateDTO calculBestCoordToAttackFromBoatChooseCell(int xTargeted, int xTargetedOpposit, int yTargeted, int yTargetedOpposite, ArrayList<CellDTO> cellsRevealed, int widthGrid, int heigthGrid) {
        if (
                isInGrid(xTargeted, yTargeted, widthGrid, heigthGrid) &&
                        getCellByCoordinate(cellsRevealed, xTargeted, yTargeted) == null
        ) {
            return new CoordinateDTO(xTargeted, yTargeted);
        }
        if (
                isInGrid(xTargetedOpposit, yTargetedOpposite, widthGrid, heigthGrid) &&
                        getCellByCoordinate(cellsRevealed, xTargetedOpposit, yTargetedOpposite) == null
        ) {
            return new CoordinateDTO(xTargetedOpposit, yTargetedOpposite);
        }

        return null;
    }

    public CoordinateDTO calculBestCoordToAttackFromBoat(ArrayList<CellDTO> cellsRevealed, int x, int y, int widthGrid, int heigthGrid) {


        int nmbBoatCellRight = countNumberOfCellBoat(cellsRevealed, x, y, 1, 0, widthGrid, heigthGrid);
        int nmbBoatCellLeft = countNumberOfCellBoat(cellsRevealed, x, y, -1, 0, widthGrid, heigthGrid);
        int nmbBoatCellBottom = countNumberOfCellBoat(cellsRevealed, x, y, 0, 1, widthGrid, heigthGrid);
        int nmbBoatCellTop = countNumberOfCellBoat(cellsRevealed, x, y, 0, -1, widthGrid, heigthGrid);


        if (nmbBoatCellRight > 1) {
            CoordinateDTO coordinateTarget = calculBestCoordToAttackFromBoatChooseCell(x + nmbBoatCellRight, x - nmbBoatCellLeft, y, y, cellsRevealed, widthGrid, heigthGrid);

            if (coordinateTarget != null) {
                return coordinateTarget;
            }
        }

        if (nmbBoatCellLeft > 1) {
            CoordinateDTO coordinateTarget = calculBestCoordToAttackFromBoatChooseCell(x - nmbBoatCellLeft, x + nmbBoatCellRight, y, y, cellsRevealed, widthGrid, heigthGrid);

            if (coordinateTarget != null) {
                return coordinateTarget;
            }
        }


        if (nmbBoatCellBottom > 1) {
            CoordinateDTO coordinateTarget = calculBestCoordToAttackFromBoatChooseCell(x, x, y + nmbBoatCellBottom, y - nmbBoatCellTop, cellsRevealed, widthGrid, heigthGrid);

            if (coordinateTarget != null) {
                return coordinateTarget;
            }
        }


        if (nmbBoatCellTop > 1) {
            CoordinateDTO coordinateTarget = calculBestCoordToAttackFromBoatChooseCell(x, x, y - nmbBoatCellTop, y + nmbBoatCellBottom, cellsRevealed, widthGrid, heigthGrid);

            if (coordinateTarget != null) {
                return coordinateTarget;
            }
        }

        return null;
    }


    public CoordinateDTO calculBestCoordToAttackFromCloud(ArrayList<CellDTO> cellsRevealed, int x, int y, int widthGrid, int heightGrid) {
        int[][] directions = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};

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
        if (cells == null) {
            return null;
        }

        Optional<CellDTO> result = cells.stream().filter(c -> c.getX() == x && c.getY() == y).findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

}
