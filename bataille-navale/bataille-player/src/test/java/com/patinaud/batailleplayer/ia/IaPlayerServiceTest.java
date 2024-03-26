package com.patinaud.batailleplayer.ia;


import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class IaPlayerServiceTest {


    private GridDTO generateEmptyGrid(int width, int height) {

        List<List<CellDTO>> grid = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            List<CellDTO> line = new ArrayList<>();
            for (int y = 0; y < height; y++) {
                CellDTO cell = new CellDTO();
                cell.setX(x);
                cell.setY(y);
                cell.setRevealed(false);
                cell.setOccupied(false);
                line.add(cell);
            }
            grid.add(line);
        }
        GridDTO gridDto = new GridDTO();
        gridDto.setCells(grid);
        return gridDto;
    }

    @Test
    void allBoatArePositionned() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<BoatType> boats = new ArrayList<BoatType>();
        boats.add(BoatType.PORTE_AVIONS);
        boats.add(BoatType.TORPILLEUR);
        boats.add(BoatType.CROISEUR);

        List<BoatDTO> boatsPositions = ia.positionBoatOnGrid(boats, generateEmptyGrid(10, 10));
        Assertions.assertEquals(3, boatsPositions.size());

        Optional<BoatDTO> porteAvion = boatsPositions.stream().filter(b -> b.getBoatType().equals(BoatType.PORTE_AVIONS)).findFirst();
        Assertions.assertTrue(porteAvion.isPresent());
        if (porteAvion.isPresent()) {
            Assertions.assertFalse(porteAvion.get().isDestroyed());
        }

        Optional<BoatDTO> torpilleur = boatsPositions.stream().filter(b -> b.getBoatType().equals(BoatType.TORPILLEUR)).findFirst();
        Assertions.assertTrue(torpilleur.isPresent());
        if (torpilleur.isPresent()) {
            Assertions.assertFalse(torpilleur.get().isDestroyed());
        }

        Optional<BoatDTO> croiseur = boatsPositions.stream().filter(b -> b.getBoatType().equals(BoatType.CROISEUR)).findFirst();
        Assertions.assertTrue(croiseur.isPresent());
        if (croiseur.isPresent()) {
            Assertions.assertFalse(croiseur.get().isDestroyed());
        }
    }

    @Test
    void theBoatIsInsideTheGridVert() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        BoatDTO boat = new BoatDTO();
        boat.setBoatType(BoatType.CROISEUR);
        boat.setxHead(2);
        boat.setyHead(4);
        boat.setHorizontal(false);

        GridDTO grid = generateEmptyGrid(10, 10);

        Assertions.assertTrue(ia.theBoatSizeCanEnterInTheGrid(boat, grid));

    }


    @Test
    void theBoatCanEnterTheGridHoriz() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        BoatDTO boat = new BoatDTO();
        boat.setBoatType(BoatType.PORTE_AVIONS);
        boat.setxHead(2);
        boat.setyHead(4);
        boat.setHorizontal(true);

        GridDTO grid = generateEmptyGrid(10, 10);

        Assertions.assertTrue(ia.theBoatSizeCanEnterInTheGrid(boat, grid));

    }


    @Test
    void theBoatCannotEnterTheGridVert() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        BoatDTO boat = new BoatDTO();
        boat.setBoatType(BoatType.SOUS_MARIN_1);
        boat.setxHead(8);
        boat.setyHead(4);
        boat.setHorizontal(false);

        GridDTO grid = generateEmptyGrid(10, 10);

        Assertions.assertTrue(ia.theBoatSizeCanEnterInTheGrid(boat, grid));

    }


    @Test
    void theBoatCanEnterTheGridVert() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        BoatDTO boat = new BoatDTO();
        boat.setBoatType(BoatType.PORTE_AVIONS);
        boat.setxHead(8);
        boat.setyHead(4);
        boat.setHorizontal(false);

        GridDTO grid = generateEmptyGrid(10, 10);

        Assertions.assertTrue(ia.theBoatSizeCanEnterInTheGrid(boat, grid));

    }


    @Test
    void boatOccupiesTheCellHoriz() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        BoatDTO boat = new BoatDTO();
        boat.setBoatType(BoatType.CROISEUR);
        boat.setxHead(5);
        boat.setyHead(5);
        boat.setHorizontal(true);

        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 5, 5));
        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 4, 4));
        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 4, 6));
        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 9, 4));
        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 9, 6));
        Assertions.assertFalse(ia.boatOccupiesTheCell(boat, 9, 7));
        Assertions.assertFalse(ia.boatOccupiesTheCell(boat, 9, 3));
    }


    @Test
    void boatOccupiesTheCellVerti() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        BoatDTO boat = new BoatDTO();
        boat.setBoatType(BoatType.TORPILLEUR);
        boat.setxHead(5);
        boat.setyHead(5);
        boat.setHorizontal(false);

        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 5, 5));
        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 5, 6));
        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 4, 4));
        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 6, 4));
        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 4, 7));
        Assertions.assertTrue(ia.boatOccupiesTheCell(boat, 6, 7));

        Assertions.assertFalse(ia.boatOccupiesTheCell(boat, 8, 7));
    }

    @Test
    void thePositionIsFree() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        BoatDTO boatToPosition = new BoatDTO();
        boatToPosition.setBoatType(BoatType.PORTE_AVIONS);
        boatToPosition.setxHead(4);
        boatToPosition.setyHead(3);
        boatToPosition.setHorizontal(true);

        BoatDTO boatAlreadyPositioned = new BoatDTO();
        boatAlreadyPositioned.setBoatType(BoatType.TORPILLEUR);
        boatAlreadyPositioned.setxHead(7);
        boatAlreadyPositioned.setyHead(5);
        boatAlreadyPositioned.setHorizontal(true);

        ArrayList<BoatDTO> boatsAlreadyPositioned = new ArrayList<>();
        boatsAlreadyPositioned.add(boatAlreadyPositioned);

        Assertions.assertTrue(ia.thePositionIsFree(boatToPosition, boatsAlreadyPositioned));
    }


    @Test
    void thePositionIsNotFree() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        BoatDTO boatToPosition = new BoatDTO();
        boatToPosition.setBoatType(BoatType.PORTE_AVIONS);
        boatToPosition.setxHead(6);
        boatToPosition.setyHead(3);
        boatToPosition.setHorizontal(true);

        BoatDTO boatAlreadyPositioned = new BoatDTO();
        boatAlreadyPositioned.setBoatType(BoatType.TORPILLEUR);
        boatAlreadyPositioned.setxHead(3);
        boatAlreadyPositioned.setyHead(3);
        boatAlreadyPositioned.setHorizontal(false);

        ArrayList<BoatDTO> boatsAlreadyPositioned = new ArrayList<>();
        boatsAlreadyPositioned.add(boatAlreadyPositioned);

        Assertions.assertTrue(ia.thePositionIsFree(boatToPosition, boatsAlreadyPositioned));
    }

    @Test
    void randomlyTargetACoveredCellNull() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        CoordinateDTO coordinate = ia.randomlyTargetACoveredCell(generateEmptyGrid(10, 10));

        Assertions.assertNotNull(coordinate);

    }


    @Test
    void cellContainsABoatNull() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        Boolean cellContainsABoat = ia.cellContainsABoat(null, 0, 0);

        Assertions.assertFalse(cellContainsABoat);

    }


    private CellDTO createRevealedCellDto(int x, int y, boolean occupied) {
        CellDTO cellDto = new CellDTO();
        cellDto.setX(x);
        cellDto.setY(y);
        cellDto.setOccupied(occupied);
        cellDto.setRevealed(true);
        return cellDto;
    }


    @Test
    void iaAttackBoat() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(4, 5, false));
        grid.updateCell(createRevealedCellDto(5, 5, true));
        grid.updateCell(createRevealedCellDto(6, 5, true));


        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(7, coordinateTargeted.getX());
        Assertions.assertEquals(5, coordinateTargeted.getY());

    }


    @Test
    void iaAttackBoatBorderRight() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(8, 5, true));
        grid.updateCell(createRevealedCellDto(9, 5, true));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(7, coordinateTargeted.getX());
        Assertions.assertEquals(5, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatBorderRightReverse() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(9, 5, true));
        grid.updateCell(createRevealedCellDto(8, 5, true));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);


        Assertions.assertEquals(7, coordinateTargeted.getX());
        Assertions.assertEquals(5, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatBorderLeft() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(0, 5, true));
        grid.updateCell(createRevealedCellDto(1, 5, true));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(2, coordinateTargeted.getX());
        Assertions.assertEquals(5, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatBorderLeftReverse() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 5, true));
        grid.updateCell(createRevealedCellDto(0, 5, true));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(2, coordinateTargeted.getX());
        Assertions.assertEquals(5, coordinateTargeted.getY());

    }


    @Test
    void iaAttackBoatBorderTop() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(3, 0, true));
        grid.updateCell(createRevealedCellDto(3, 1, true));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);


        Assertions.assertEquals(3, coordinateTargeted.getX());
        Assertions.assertEquals(2, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatBorderTopReverse() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(3, 1, true));
        grid.updateCell(createRevealedCellDto(3, 0, true));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(3, coordinateTargeted.getX());
        Assertions.assertEquals(2, coordinateTargeted.getY());

    }


    @Test
    void iaAttackBoatBorderBottom() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(3, 8, true));
        grid.updateCell(createRevealedCellDto(3, 9, true));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(3, coordinateTargeted.getX());
        Assertions.assertEquals(7, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatBorderBottomReverse() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(3, 9, true));
        grid.updateCell(createRevealedCellDto(3, 8, true));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(3, coordinateTargeted.getX());
        Assertions.assertEquals(7, coordinateTargeted.getY());

    }


    @Test
    void iaAttackBoatTop() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 3, true));
        grid.updateCell(createRevealedCellDto(1, 4, true));
        grid.updateCell(createRevealedCellDto(1, 5, false));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(1, coordinateTargeted.getX());
        Assertions.assertEquals(2, coordinateTargeted.getY());

    }


    @Test
    void iaAttackCloud() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(6, 5, false));
        grid.updateCell(createRevealedCellDto(4, 5, false));
        grid.updateCell(createRevealedCellDto(5, 4, false));
        grid.updateCell(createRevealedCellDto(5, 5, true));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(5, coordinateTargeted.getX());
        Assertions.assertEquals(6, coordinateTargeted.getY());

    }


    @Test
    void iaAttackCloudBorderRight() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(9, 2, false));
        grid.updateCell(createRevealedCellDto(9, 3, true));
        grid.updateCell(createRevealedCellDto(9, 4, false));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(8, coordinateTargeted.getX());
        Assertions.assertEquals(3, coordinateTargeted.getY());

    }


    @Test
    void iaAttackCloudBorderLeft() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(0, 3, true));
        grid.updateCell(createRevealedCellDto(1, 3, false));
        grid.updateCell(createRevealedCellDto(0, 2, false));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(0, coordinateTargeted.getX());
        Assertions.assertEquals(4, coordinateTargeted.getY());

    }


    @Test
    void iaAttackCloudBorderBottom() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(5, 9, true));
        grid.updateCell(createRevealedCellDto(4, 9, false));
        grid.updateCell(createRevealedCellDto(6, 9, false));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(5, coordinateTargeted.getX());
        Assertions.assertEquals(8, coordinateTargeted.getY());

    }


    @Test
    void iaAttackCloudBorderTop() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(5, 0, true));
        grid.updateCell(createRevealedCellDto(4, 0, false));
        grid.updateCell(createRevealedCellDto(6, 0, false));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(5, coordinateTargeted.getX());
        Assertions.assertEquals(1, coordinateTargeted.getY());

    }


    @Test
    void iaAttackRandom() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(3, 2);
        grid.updateCell(createRevealedCellDto(0, 0, false));
        grid.updateCell(createRevealedCellDto(1, 0, false));
        grid.updateCell(createRevealedCellDto(2, 0, false));
        grid.updateCell(createRevealedCellDto(0, 1, false));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertTrue(coordinateTargeted.getX() == 1 || coordinateTargeted.getX() == 2);
        Assertions.assertEquals(1, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatIsolatedAndRandomBorder() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(5, 0, true));
        grid.updateCell(createRevealedCellDto(6, 0, false));
        grid.updateCell(createRevealedCellDto(4, 0, false));

        grid.updateCell(createRevealedCellDto(5, 1, true));
        grid.updateCell(createRevealedCellDto(6, 1, false));
        grid.updateCell(createRevealedCellDto(4, 1, false));

        grid.updateCell(createRevealedCellDto(5, 2, false));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertNotNull(coordinateTargeted);
        Assertions.assertTrue(coordinateTargeted.getX() >= 0 && coordinateTargeted.getX() < 10);
        Assertions.assertTrue(coordinateTargeted.getY() >= 0 && coordinateTargeted.getY() < 10);
    }


    @Test
    void iaAttackBoatIsolatedAndRandomInGrid() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);


        grid.updateCell(createRevealedCellDto(1, 4, false));

        grid.updateCell(createRevealedCellDto(8, 1, false));

        grid.updateCell(createRevealedCellDto(7, 4, true));
        grid.updateCell(createRevealedCellDto(8, 4, false));
        grid.updateCell(createRevealedCellDto(6, 4, false));
        grid.updateCell(createRevealedCellDto(7, 5, false));


        grid.updateCell(createRevealedCellDto(7, 3, true));
        grid.updateCell(createRevealedCellDto(7, 2, true));
        grid.updateCell(createRevealedCellDto(7, 1, true));


        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(7, coordinateTargeted.getX());
        Assertions.assertEquals(0, coordinateTargeted.getY());
    }


    @Test
    void iaAttackBoatAlone() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(0, 0, true));
        grid.updateCell(createRevealedCellDto(1, 0, false));
        grid.updateCell(createRevealedCellDto(0, 1, false));

        grid.updateCell(createRevealedCellDto(8, 2, true));
        grid.updateCell(createRevealedCellDto(8, 3, true));

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid);

        Assertions.assertEquals(8, coordinateTargeted.getX());
        Assertions.assertEquals(4, coordinateTargeted.getY());

    }


    @Test
    void calculBestCoordToAttackFromCellXplus1() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 1, true));


        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromOneUncoverBoatCell(grid, createRevealedCellDto(1, 1, true));

        Assertions.assertEquals(2, coordinateTargted.getX());
        Assertions.assertEquals(1, coordinateTargted.getY());
    }


    @Test
    void calculBestCoordToAttackFromCellXMoins1() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 1, true));
        grid.updateCell(createRevealedCellDto(2, 1, false));
        grid.updateCell(createRevealedCellDto(1, 0, false));
        grid.updateCell(createRevealedCellDto(1, 2, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromOneUncoverBoatCell(grid, createRevealedCellDto(1, 1, true));

        Assertions.assertEquals(0, coordinateTargted.getX());
        Assertions.assertEquals(1, coordinateTargted.getY());
    }


    @Test
    void calculBestCoordToAttackFromCellYplus1() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 1, true));
        grid.updateCell(createRevealedCellDto(2, 1, false));
        grid.updateCell(createRevealedCellDto(0, 1, false));
        grid.updateCell(createRevealedCellDto(1, 0, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromOneUncoverBoatCell(grid, createRevealedCellDto(1, 1, true));

        Assertions.assertEquals(1, coordinateTargted.getX());
        Assertions.assertEquals(2, coordinateTargted.getY());
    }


    @Test
    void calculBestCoordToAttackFromCellYMoins1() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 1, true));
        grid.updateCell(createRevealedCellDto(2, 1, false));
        grid.updateCell(createRevealedCellDto(0, 1, false));
        grid.updateCell(createRevealedCellDto(1, 2, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromOneUncoverBoatCell(grid, createRevealedCellDto(1, 1, true));

        Assertions.assertEquals(1, coordinateTargted.getX());
        Assertions.assertEquals(0, coordinateTargted.getY());
    }


}
