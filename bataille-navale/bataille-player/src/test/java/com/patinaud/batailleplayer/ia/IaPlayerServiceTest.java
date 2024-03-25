package com.patinaud.batailleplayer.ia;


import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class IaPlayerServiceTest {

    @Test
    void allBoatArePositionned() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<BoatType> boats = new ArrayList<BoatType>();
        boats.add(BoatType.PORTE_AVIONS);
        boats.add(BoatType.TORPILLEUR);
        boats.add(BoatType.CROISEUR);

        List<BoatDTO> boatsPositions = ia.positionBoatOnGrid(boats, 10, 10);
        Assertions.assertEquals(3, boatsPositions.size());

        Optional<BoatDTO> porteAvions = boatsPositions.stream().filter(b -> b.getBoatType().equals(BoatType.PORTE_AVIONS)).findFirst();
        Assertions.assertTrue(porteAvions.isPresent());
        if (porteAvions.isPresent()) {
            Assertions.assertFalse(porteAvions.get().isDestroyed());
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
    void theHeadofTheBoatIsWellPosition() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        Assertions.assertTrue(ia.theBoatIsPositionedInsideTheGrid(BoatType.CROISEUR, 1, 1, false, 10, 10));
        Assertions.assertTrue(ia.theBoatIsPositionedInsideTheGrid(BoatType.CROISEUR, 0, 0, false, 10, 10));

        Assertions.assertFalse(ia.theBoatIsPositionedInsideTheGrid(BoatType.CROISEUR, -1, 1, false, 10, 10));
        Assertions.assertFalse(ia.theBoatIsPositionedInsideTheGrid(BoatType.CROISEUR, 0, -1, false, 10, 10));
        Assertions.assertFalse(ia.theBoatIsPositionedInsideTheGrid(BoatType.CROISEUR, 10, 1, false, 10, 10));
        Assertions.assertFalse(ia.theBoatIsPositionedInsideTheGrid(BoatType.CROISEUR, 0, 10, false, 10, 10));

    }


    @Test
    void theBoatIsPositionnedInsideTheGridHoriz() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        Assertions.assertTrue(ia.theBoatIsPositionedInsideTheGrid(BoatType.PORTE_AVIONS, 2, 3, true, 10, 10));
    }


    @Test
    void theBoatIsNotPositionnedInsideTheGridHoriz() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        Assertions.assertFalse(ia.theBoatIsPositionedInsideTheGrid(BoatType.PORTE_AVIONS, 9, 3, true, 10, 10));
    }


    @Test
    void theBoatIsPositionnedInsideTheGridVertical() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        Assertions.assertTrue(ia.theBoatIsPositionedInsideTheGrid(BoatType.PORTE_AVIONS, 2, 3, false, 10, 10));
    }


    @Test
    void theBoatIsNotPositionnedInsideTheGridVertical() {
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        Assertions.assertFalse(ia.theBoatIsPositionedInsideTheGrid(BoatType.PORTE_AVIONS, 4, 8, false, 10, 10));
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
        BoatDTO boat = new BoatDTO();
        boat.setBoatType(BoatType.TORPILLEUR);
        boat.setxHead(7);
        boat.setyHead(3);
        boat.setHorizontal(true);

        ArrayList<BoatDTO> boatsAlreadyPositionned = new ArrayList<>();
        boatsAlreadyPositionned.add(boat);

        Assertions.assertTrue(ia.thePositionIsFree(BoatType.PORTE_AVIONS, boatsAlreadyPositionned, 4, 2, false));
    }


    @Test
    void thePositionIsNotFree() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        BoatDTO boat = new BoatDTO();
        boat.setBoatType(BoatType.TORPILLEUR);
        boat.setxHead(5);
        boat.setyHead(5);
        boat.setHorizontal(false);

        ArrayList<BoatDTO> boatsAlreadyPositionned = new ArrayList<>();
        boatsAlreadyPositionned.add(boat);

        Assertions.assertFalse(ia.thePositionIsFree(BoatType.PORTE_AVIONS, boatsAlreadyPositionned, 4, 6, false));
    }

    @Test
    void randomlyTargetACoveredCellNull() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();
        CoordinateDTO coordinate = ia.randomlyTargetACoveredCell(null, 10, 10);

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

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(4, 5, false));
        cellsRevealed.add(createRevealedCellDto(5, 5, true));
        cellsRevealed.add(createRevealedCellDto(6, 5, true));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(7, coordinateTargted.getX());
        Assertions.assertEquals(5, coordinateTargted.getY());

    }


    @Test
    void iaAttackBoatBorderRight() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(8, 5, true));
        cellsRevealed.add(createRevealedCellDto(9, 5, true));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(7, coordinateTargted.getX());
        Assertions.assertEquals(5, coordinateTargted.getY());

    }

    @Test
    void iaAttackBoatBorderRightReverse() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(9, 5, true));
        cellsRevealed.add(createRevealedCellDto(8, 5, true));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(7, coordinateTargted.getX());
        Assertions.assertEquals(5, coordinateTargted.getY());

    }

    @Test
    void iaAttackBoatBorderLeft() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(0, 5, true));
        cellsRevealed.add(createRevealedCellDto(1, 5, true));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(2, coordinateTargted.getX());
        Assertions.assertEquals(5, coordinateTargted.getY());

    }

    @Test
    void iaAttackBoatBorderLeftReverse() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(1, 5, true));
        cellsRevealed.add(createRevealedCellDto(0, 5, true));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(2, coordinateTargted.getX());
        Assertions.assertEquals(5, coordinateTargted.getY());

    }


    @Test
    void iaAttackBoatBorderTop() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(3, 0, true));
        cellsRevealed.add(createRevealedCellDto(3, 1, true));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(3, coordinateTargted.getX());
        Assertions.assertEquals(2, coordinateTargted.getY());

    }

    @Test
    void iaAttackBoatBorderTopReverse() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(3, 1, true));
        cellsRevealed.add(createRevealedCellDto(3, 0, true));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(3, coordinateTargted.getX());
        Assertions.assertEquals(2, coordinateTargted.getY());

    }


    @Test
    void iaAttackBoatBorderBottom() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(3, 8, true));
        cellsRevealed.add(createRevealedCellDto(3, 9, true));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(3, coordinateTargted.getX());
        Assertions.assertEquals(7, coordinateTargted.getY());

    }

    @Test
    void iaAttackBoatBorderBottomReverse() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(3, 9, true));
        cellsRevealed.add(createRevealedCellDto(3, 8, true));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(3, coordinateTargted.getX());
        Assertions.assertEquals(7, coordinateTargted.getY());

    }


    @Test
    void iaAttackBoatTop() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(1, 3, true));
        cellsRevealed.add(createRevealedCellDto(1, 4, true));
        cellsRevealed.add(createRevealedCellDto(1, 5, false));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(1, coordinateTargted.getX());
        Assertions.assertEquals(2, coordinateTargted.getY());

    }


    @Test
    void iaAttackCloud() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(5, 5, true));
        cellsRevealed.add(createRevealedCellDto(6, 5, false));
        cellsRevealed.add(createRevealedCellDto(4, 5, false));
        cellsRevealed.add(createRevealedCellDto(5, 4, false));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(5, coordinateTargted.getX());
        Assertions.assertEquals(6, coordinateTargted.getY());

    }


    @Test
    void iaAttackCloudBorderRight() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(9, 3, true));
        cellsRevealed.add(createRevealedCellDto(9, 2, false));
        cellsRevealed.add(createRevealedCellDto(9, 4, false));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(8, coordinateTargted.getX());
        Assertions.assertEquals(3, coordinateTargted.getY());

    }


    @Test
    void iaAttackCloudBorderLeft() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(0, 3, true));
        cellsRevealed.add(createRevealedCellDto(1, 3, false));
        cellsRevealed.add(createRevealedCellDto(0, 2, false));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(0, coordinateTargted.getX());
        Assertions.assertEquals(4, coordinateTargted.getY());

    }


    @Test
    void iaAttackCloudBorderBottom() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(5, 9, true));
        cellsRevealed.add(createRevealedCellDto(4, 9, false));
        cellsRevealed.add(createRevealedCellDto(6, 9, false));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(5, coordinateTargted.getX());
        Assertions.assertEquals(8, coordinateTargted.getY());

    }


    @Test
    void iaAttackCloudBorderTop() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(5, 0, true));
        cellsRevealed.add(createRevealedCellDto(4, 0, false));
        cellsRevealed.add(createRevealedCellDto(6, 0, false));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(5, coordinateTargted.getX());
        Assertions.assertEquals(1, coordinateTargted.getY());

    }


    @Test
    void iaAttackRandom() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(0, 0, false));
        cellsRevealed.add(createRevealedCellDto(1, 0, false));
        cellsRevealed.add(createRevealedCellDto(2, 0, false));
        cellsRevealed.add(createRevealedCellDto(0, 1, false));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 3, 2);

        Assertions.assertTrue(coordinateTargted.getX() == 1 || coordinateTargted.getX() == 2);
        Assertions.assertEquals(1, coordinateTargted.getY());

    }

    @Test
    void iaAttackBoatIsolatedAndRandomBorder() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(5, 0, true));
        cellsRevealed.add(createRevealedCellDto(6, 0, false));
        cellsRevealed.add(createRevealedCellDto(4, 0, false));

        cellsRevealed.add(createRevealedCellDto(5, 1, true));
        cellsRevealed.add(createRevealedCellDto(6, 1, false));
        cellsRevealed.add(createRevealedCellDto(4, 1, false));

        cellsRevealed.add(createRevealedCellDto(5, 2, false));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertNotNull(coordinateTargted);
        Assertions.assertTrue(coordinateTargted.getX() >= 0 && coordinateTargted.getX() < 10);
        Assertions.assertTrue(coordinateTargted.getY() >= 0 && coordinateTargted.getY() < 10);
    }


    @Test
    void iaAttackBoatIsolatedAndRandomInGrid() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();


        cellsRevealed.add(createRevealedCellDto(1, 4, false));

        cellsRevealed.add(createRevealedCellDto(8, 1, false));

        cellsRevealed.add(createRevealedCellDto(7, 4, true));
        cellsRevealed.add(createRevealedCellDto(8, 4, false));
        cellsRevealed.add(createRevealedCellDto(6, 4, false));
        cellsRevealed.add(createRevealedCellDto(7, 5, false));


        cellsRevealed.add(createRevealedCellDto(7, 3, true));
        cellsRevealed.add(createRevealedCellDto(7, 2, true));
        cellsRevealed.add(createRevealedCellDto(7, 1, true));


        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(7, coordinateTargted.getX());
        Assertions.assertEquals(0, coordinateTargted.getY());
    }


    @Test
    void iaAttackBoatAlone() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(0, 0, true));
        cellsRevealed.add(createRevealedCellDto(1, 0, false));
        cellsRevealed.add(createRevealedCellDto(0, 1, false));

        cellsRevealed.add(createRevealedCellDto(8, 2, true));
        cellsRevealed.add(createRevealedCellDto(8, 3, true));

        CoordinateDTO coordinateTargted = ia.iaAttack(cellsRevealed, 10, 10);

        Assertions.assertEquals(8, coordinateTargted.getX());
        Assertions.assertEquals(4, coordinateTargted.getY());

    }


    @Test
    void calculBestCoordToAttackFromCellXplus1() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(1, 1, true));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromCloud(cellsRevealed, 1, 1, 3, 3);

        Assertions.assertEquals(2, coordinateTargted.getX());
        Assertions.assertEquals(1, coordinateTargted.getY());
    }


    @Test
    void calculBestCoordToAttackFromCellXMoins1() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(1, 1, true));
        cellsRevealed.add(createRevealedCellDto(2, 1, false));
        cellsRevealed.add(createRevealedCellDto(1, 0, false));
        cellsRevealed.add(createRevealedCellDto(1, 2, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromCloud(cellsRevealed, 1, 1, 3, 3);

        Assertions.assertEquals(0, coordinateTargted.getX());
        Assertions.assertEquals(1, coordinateTargted.getY());
    }


    @Test
    void calculBestCoordToAttackFromCellYplus1() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(1, 1, true));
        cellsRevealed.add(createRevealedCellDto(2, 1, false));
        cellsRevealed.add(createRevealedCellDto(0, 1, false));
        cellsRevealed.add(createRevealedCellDto(1, 0, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromCloud(cellsRevealed, 1, 1, 3, 3);

        Assertions.assertEquals(1, coordinateTargted.getX());
        Assertions.assertEquals(2, coordinateTargted.getY());
    }


    @Test
    void calculBestCoordToAttackFromCellYMoins1() {

        IaPlayerServiceImpl ia = new IaPlayerServiceImpl();

        ArrayList<CellDTO> cellsRevealed = new ArrayList();
        cellsRevealed.add(createRevealedCellDto(1, 1, true));
        cellsRevealed.add(createRevealedCellDto(2, 1, false));
        cellsRevealed.add(createRevealedCellDto(0, 1, false));
        cellsRevealed.add(createRevealedCellDto(1, 2, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromCloud(cellsRevealed, 1, 1, 3, 3);

        Assertions.assertEquals(1, coordinateTargted.getX());
        Assertions.assertEquals(0, coordinateTargted.getY());
    }


}
