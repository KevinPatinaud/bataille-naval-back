package com.patinaud.batailleplayer.ia;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import com.patinaud.batailleplayer.model.PonderationCell;
import com.patinaud.batailleservice.service.GridService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IaPlayerServiceImpl implements IaPlayerService {

    private static final Logger logger = LoggerFactory.getLogger(IaPlayerServiceImpl.class);

    private final Random random = new Random();
    private final GridService gridService;

    @Autowired
    public IaPlayerServiceImpl(GridService gridService) {
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

        if (gridService.theBoatCanBePositionHere(boatToPosition, alreadyPositionnedBoats, grid)) {
            return boatToPosition;
        }

        return findAPositionForTheBoat(boatType, alreadyPositionnedBoats, grid);

    }


    public CoordinateDTO iaAttack(GridDTO grid, List<BoatType> boatsTypesToFinds) {

        CoordinateDTO coordinateToAttack = calculBestCoordToAttack(grid, boatsTypesToFinds);
        if (coordinateToAttack != null) {
            return coordinateToAttack;
        }

        return randomlyTargetAnUnrevealedCell(grid, boatsTypesToFinds);
    }


    public CoordinateDTO calculBestCoordToAttack(GridDTO grid, List<BoatType> boatsTypesToFinds) {

        Optional<CoordinateDTO> coordinateToAttack = grid.getCells().stream().flatMap(List::stream).filter(cell -> cell.isRevealed() && cell.isOccupied())
                .map(cell -> calculBestCoordToAttackFromAtLeastTwoRevealedBoatCell(grid, cell))
                .filter(Objects::nonNull)
                .findFirst();

        if (coordinateToAttack.isPresent()) {
            return coordinateToAttack.get();
        }


        coordinateToAttack = grid.getCells().stream().flatMap(List::stream).filter(cell -> cell.isRevealed() && cell.isOccupied())
                .map(cell -> calculBestCoordToAttackFromOneUncoverBoatCell(grid, boatsTypesToFinds, cell))
                .filter(Objects::nonNull)
                .findFirst();

        if (coordinateToAttack.isPresent()) {
            return coordinateToAttack.get();
        }

        return null;
    }


    public CoordinateDTO randomlyTargetAnUnrevealedCell(GridDTO grid, List<BoatType> boatsTypesToFinds) {

        ArrayList<PonderationCell> ponderations = new ArrayList<>();
        int totalWeight = 0;

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                if (!grid.getCell(x, y).isRevealed()) {

                    int weight = gridService.countNumberOfPositionBoatsCanTakeInTheCoordinate(grid, boatsTypesToFinds, new CoordinateDTO(x, y));

                    if (weight > 0) {
                        totalWeight = totalWeight + weight;
                        ponderations.add(new PonderationCell(weight, x, y));
                    }
                }
            }
        }

        if (totalWeight > 0) {
            int indexWeightCell = random.nextInt(totalWeight);
            int recoveryWeight = 0;
            for (int i = 0; i < ponderations.size(); i++) {
                recoveryWeight = recoveryWeight + ponderations.get(i).getWeight();

                if (recoveryWeight > indexWeightCell) {
                    return new CoordinateDTO(ponderations.get(i).getX(), ponderations.get(i).getY());
                }
            }
        }

        return null;
    }


    public CoordinateDTO calculBestCoordToAttackFromAtLeastTwoRevealedBoatCell(GridDTO grid, CellDTO cell) {

        int nmbBoatCellRight = gridService.countNumberOfRevealedCellWhichContainsABoatFromThisCoordinate(grid, new CoordinateDTO(cell.getX(), cell.getY()), 1, 0) + 1;
        int nmbBoatCellLeft = gridService.countNumberOfRevealedCellWhichContainsABoatFromThisCoordinate(grid, new CoordinateDTO(cell.getX(), cell.getY()), -1, 0) + 1;
        int nmbBoatCellBottom = gridService.countNumberOfRevealedCellWhichContainsABoatFromThisCoordinate(grid, new CoordinateDTO(cell.getX(), cell.getY()), 0, 1) + 1;
        int nmbBoatCellTop = gridService.countNumberOfRevealedCellWhichContainsABoatFromThisCoordinate(grid, new CoordinateDTO(cell.getX(), cell.getY()), 0, -1) + 1;

        logger.info("calculBestCoordToAttackFromAtLeastTwoRevealedBoatCell");
        logger.info("cell.getX() : " + cell.getX());
        logger.info("cell.getY() : " + cell.getY());
        logger.info("nmbBoatCellRight : " + nmbBoatCellRight);
        logger.info("nmbBoatCellLeft : " + nmbBoatCellLeft);
        logger.info("nmbBoatCellBottom : " + nmbBoatCellBottom);
        logger.info("nmbBoatCellTop : " + nmbBoatCellTop);

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
            return chooseBetterCellToAttackBetweenTwo(new CoordinateDTO(cell.getX(), cell.getY() - nmbBoatCellTop), new CoordinateDTO(cell.getX(), cell.getY() + nmbBoatCellBottom), grid);

        }

        return null;
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

    public CoordinateDTO calculBestCoordToAttackFromOneUncoverBoatCell(GridDTO grid, List<BoatType> boatsTypesToFinds, CellDTO cell) {

        int numberOfHorizontalPosition =
                boatsTypesToFinds.stream().map(boatType ->
                        gridService.countNumberOfPositionTheBoatCanTakeInTheCoordinateWithDirection(grid, boatType, new CoordinateDTO(cell.getX(), cell.getY()), true)
                ).mapToInt(Integer::intValue).sum();

        int numberOfVerticalPosition =
                boatsTypesToFinds.stream().map(boatType ->
                        gridService.countNumberOfPositionTheBoatCanTakeInTheCoordinateWithDirection(grid, boatType, new CoordinateDTO(cell.getX(), cell.getY()), false)
                ).mapToInt(Integer::intValue).sum();

        if (numberOfHorizontalPosition > numberOfVerticalPosition && numberOfHorizontalPosition > 0) {
            int numberUnrevealedCellLeft = gridService.countNumberOfUnrevealedCellFromThisCoordinate(grid, new CoordinateDTO(cell.getX(), cell.getY()), -1, 0);
            int numberUnrevealedCellRight = gridService.countNumberOfUnrevealedCellFromThisCoordinate(grid, new CoordinateDTO(cell.getX(), cell.getY()), 1, 0);

            if (numberUnrevealedCellLeft > numberUnrevealedCellRight) {
                return new CoordinateDTO(cell.getX() - 1, cell.getY());
            }

            return new CoordinateDTO(cell.getX() + 1, cell.getY());
        }

        if (numberOfVerticalPosition > 0) {
            int numberUnrevealedCellBottom = gridService.countNumberOfUnrevealedCellFromThisCoordinate(grid, new CoordinateDTO(cell.getX(), cell.getY()), 0, 1);
            int numberUnrevealedCellTop = gridService.countNumberOfUnrevealedCellFromThisCoordinate(grid, new CoordinateDTO(cell.getX(), cell.getY()), 0, -1);

            if (numberUnrevealedCellBottom > numberUnrevealedCellTop) {
                return new CoordinateDTO(cell.getX(), cell.getY() + 1);
            }
            return new CoordinateDTO(cell.getX(), cell.getY() - 1);
        }

        return null;

    }


}
