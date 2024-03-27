package com.patinaud.batailleplayer.ia;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import com.patinaud.batailleplayer.model.PonderationCell;
import com.patinaud.batailleservice.service.GridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IaPlayerServiceImpl implements IaPlayerService {

    private GridService gridService;
    private final Random random = new Random();

    @Autowired
    public IaPlayerServiceImpl(GridService gridService)
    {
        this.gridService = gridService;
    }

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

        if (gridService.theBoatCanBePositionHere(boatToPosition , alreadyPositionnedBoats , grid))
        {
            return boatToPosition;
        }

        return findAPositionForTheBoat(boatType, alreadyPositionnedBoats, grid);

    }


    public CoordinateDTO iaAttack(GridDTO grid, List<BoatType> boatsToFinds) {

        CoordinateDTO coordinateToAttack = calculBestCoordToAttack(grid);
        if (coordinateToAttack != null) {
            return coordinateToAttack;
        }

        return randomlyTargetACoveredCell(grid);
    }


    public CoordinateDTO calculBestCoordToAttack(GridDTO grid) {

        Optional<CoordinateDTO> coordinateToAttack = grid.getCells().stream().flatMap(List::stream).filter(cell -> cell.isRevealed() && cell.isOccupied())
                .map(cell -> calculBestCoordToAttackFromAtLeastTwoRevealedBoatCell(grid, cell))
                .filter(Objects::nonNull)
                .findFirst();

        if (coordinateToAttack.isPresent()) {
            return coordinateToAttack.get();
        }


        coordinateToAttack = grid.getCells().stream().flatMap(List::stream).filter(cell -> cell.isRevealed() && cell.isOccupied())
                .map(cell -> calculBestCoordToAttackFromOneUncoverBoatCell(grid, cell))
                .filter(Objects::nonNull)
                .findFirst();

        if (coordinateToAttack.isPresent()) {
            return coordinateToAttack.get();
        }

        return null;
    }


    public CoordinateDTO randomlyTargetACoveredCell(GridDTO grid) {

        ArrayList<PonderationCell> ponderations = new ArrayList<>();
        int totalWeight = 0;

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                if (!grid.getCell(x, y).isRevealed()) {

                    int weight = calculateWeight(grid, x, y);

                    if (weight > 1) {
                        totalWeight = totalWeight + weight;
                        ponderations.add(new PonderationCell(weight, x, y));
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


    private int calculateWeight(GridDTO grid, int x, int y) {
        int weight = 1;
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : directions) {
            if (grid.isInTheGrid(x + dir[0], y + dir[1]) && !grid.getCell(x + dir[0], y + dir[1]).isRevealed()) {
                weight *= 2;
            }
        }
        return weight;
    }


    public CoordinateDTO calculBestCoordToAttackFromAtLeastTwoRevealedBoatCell(GridDTO grid, CellDTO cell) {

        int nmbBoatCellRight = countNumberOfCellBoatFromRevealedBoatCell(grid, cell, 1, 0);
        int nmbBoatCellLeft = countNumberOfCellBoatFromRevealedBoatCell(grid, cell, -1, 0);
        int nmbBoatCellBottom = countNumberOfCellBoatFromRevealedBoatCell(grid, cell, 0, 1);
        int nmbBoatCellTop = countNumberOfCellBoatFromRevealedBoatCell(grid, cell, 0, -1);


        if (nmbBoatCellRight > 1) {
            CoordinateDTO coordinateTarget = chooseBetterCellToAttackBetweenTwo(new CoordinateDTO(cell.getX() + nmbBoatCellRight, cell.getY()), new CoordinateDTO(cell.getX() - nmbBoatCellLeft, cell.getY()), grid);

            if (coordinateTarget != null) {
                return coordinateTarget;
            }
        }

        if (nmbBoatCellLeft > 1) {
            CoordinateDTO coordinateTarget = chooseBetterCellToAttackBetweenTwo(new CoordinateDTO(cell.getX() - nmbBoatCellLeft, cell.getY()), new CoordinateDTO(cell.getX() + nmbBoatCellRight, cell.getY()), grid);

            if (coordinateTarget != null) {
                return coordinateTarget;
            }
        }


        if (nmbBoatCellBottom > 1) {
            CoordinateDTO coordinateTarget = chooseBetterCellToAttackBetweenTwo(new CoordinateDTO(cell.getX(), cell.getY() + nmbBoatCellBottom), new CoordinateDTO(cell.getX(), cell.getY() - nmbBoatCellTop), grid);

            if (coordinateTarget != null) {
                return coordinateTarget;
            }
        }


        if (nmbBoatCellTop > 1) {
            CoordinateDTO coordinateTarget = chooseBetterCellToAttackBetweenTwo(new CoordinateDTO(cell.getX(), cell.getY() - nmbBoatCellTop), new CoordinateDTO(cell.getX(), cell.getY() + nmbBoatCellBottom), grid);

            if (coordinateTarget != null) {
                return coordinateTarget;
            }
        }

        return null;
    }


    public int countNumberOfCellBoatFromRevealedBoatCell(GridDTO grid, CellDTO cell, int evolveX, int evolveY) {
        int inc = 0;
        int x = cell.getX() + evolveX * inc;
        int y = cell.getY() + evolveY * inc;

        while (
                grid.isInTheGrid(x, y) &&
                        grid.getCell(x, y).isRevealed() &&
                        grid.getCell(x, y).isOccupied()) {
            inc++;
            x = cell.getX() + evolveX * inc;
            y = cell.getY() + evolveY * inc;
        }
        return inc;
    }


    public CoordinateDTO chooseBetterCellToAttackBetweenTwo(CoordinateDTO coordinateCellInDirection, CoordinateDTO coordinateCellInOppositeDirection, GridDTO grid) {
        if (
                grid.isInTheGrid(coordinateCellInDirection) &&
                        !grid.getCell(coordinateCellInDirection).isRevealed()
        ) {
            return coordinateCellInDirection;
        }
        if (
                grid.isInTheGrid(coordinateCellInOppositeDirection) &&
                        !grid.getCell(coordinateCellInOppositeDirection).isRevealed()
        ) {
            return coordinateCellInOppositeDirection;
        }

        return null;
    }

    public CoordinateDTO calculBestCoordToAttackFromOneUncoverBoatCell(GridDTO grid, CellDTO cell) {
        int[][] directions = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};

        for (int[] dir : directions) {
            int targetX = cell.getX() + dir[0];
            int targetY = cell.getY() + dir[1];

            if (grid.isInTheGrid(targetX, targetY) && !grid.getCell(targetX, targetY).isRevealed()) {
                return new CoordinateDTO(targetX, targetY);
            }
        }

        return null;
    }


}
