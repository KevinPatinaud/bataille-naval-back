package com.patinaud.batailleplayer.ia;


import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import com.patinaud.batailleservice.service.GridService;
import com.patinaud.batailleservice.service.GridServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    void randomlyTargetACoveredCellNull() {
        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);
        CoordinateDTO coordinate = ia.randomlyTargetAnUnrevealedCell(generateEmptyGrid(10, 10), List.of(BoatType.SOUS_MARIN_1));

        Assertions.assertNotNull(coordinate);

    }

    @Test
    void randomlyTargetAnUnrevealedCellGridFull() {

        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);
        CoordinateDTO coordinate = ia.randomlyTargetAnUnrevealedCell(generateEmptyGrid(4, 4), List.of(BoatType.PORTE_AVIONS));

        Assertions.assertNull(coordinate);
    }


    @Test
    void allBoatArePositionned() {
        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

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

        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(4, 5, false));
        grid.updateCell(createRevealedCellDto(5, 5, true));
        grid.updateCell(createRevealedCellDto(6, 5, true));


        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(7, coordinateTargeted.getX());
        Assertions.assertEquals(5, coordinateTargeted.getY());

    }


    @Test
    void iaAttackBoatBorderRight() {

        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(8, 5, true));
        grid.updateCell(createRevealedCellDto(9, 5, true));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(7, coordinateTargeted.getX());
        Assertions.assertEquals(5, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatBorderRightReverse() {

        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(9, 5, true));
        grid.updateCell(createRevealedCellDto(8, 5, true));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);


        Assertions.assertEquals(7, coordinateTargeted.getX());
        Assertions.assertEquals(5, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatBorderLeft() {

        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(5, 5, true));
        grid.updateCell(createRevealedCellDto(6, 5, true));
        grid.updateCell(createRevealedCellDto(7, 5, false));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(4, coordinateTargeted.getX());
        Assertions.assertEquals(5, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatBorderLeftReverse() {

        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(6, 5, true));
        grid.updateCell(createRevealedCellDto(5, 5, true));
        grid.updateCell(createRevealedCellDto(7, 5, false));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(4, coordinateTargeted.getX());
        Assertions.assertEquals(5, coordinateTargeted.getY());

    }


    @Test
    void iaAttackBoatBorderTop() {

        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(3, 0, true));
        grid.updateCell(createRevealedCellDto(3, 1, true));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);


        Assertions.assertEquals(3, coordinateTargeted.getX());
        Assertions.assertEquals(2, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatBorderTopReverse() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(3, 1, true));
        grid.updateCell(createRevealedCellDto(3, 0, true));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(3, coordinateTargeted.getX());
        Assertions.assertEquals(2, coordinateTargeted.getY());

    }


    @Test
    void iaAttackBoatBorderBottom() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(3, 8, true));
        grid.updateCell(createRevealedCellDto(3, 9, true));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(3, coordinateTargeted.getX());
        Assertions.assertEquals(7, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatBorderBottomReverse() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(3, 9, true));
        grid.updateCell(createRevealedCellDto(3, 8, true));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(3, coordinateTargeted.getX());
        Assertions.assertEquals(7, coordinateTargeted.getY());

    }


    @Test
    void iaAttackBoatTop() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 3, true));
        grid.updateCell(createRevealedCellDto(1, 4, true));
        grid.updateCell(createRevealedCellDto(1, 5, false));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(1, coordinateTargeted.getX());
        Assertions.assertEquals(2, coordinateTargeted.getY());

    }


    @Test
    void iaAttackCloud() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);


        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(6, 5, false));
        grid.updateCell(createRevealedCellDto(4, 5, false));
        grid.updateCell(createRevealedCellDto(5, 4, false));
        grid.updateCell(createRevealedCellDto(5, 5, true));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(5, coordinateTargeted.getX());
        Assertions.assertEquals(6, coordinateTargeted.getY());

    }


    @Test
    void iaAttackCloudBorderRight() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(9, 2, false));
        grid.updateCell(createRevealedCellDto(9, 3, true));
        grid.updateCell(createRevealedCellDto(9, 4, false));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(8, coordinateTargeted.getX());
        Assertions.assertEquals(3, coordinateTargeted.getY());

    }


    @Test
    void iaAttackCloudBorderLeft() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(0, 3, true));
        grid.updateCell(createRevealedCellDto(1, 3, false));
        grid.updateCell(createRevealedCellDto(0, 2, false));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(0, coordinateTargeted.getX());
        Assertions.assertEquals(4, coordinateTargeted.getY());

    }


    @Test
    void iaAttackCloudBorderBottom() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(5, 9, true));
        grid.updateCell(createRevealedCellDto(4, 9, false));
        grid.updateCell(createRevealedCellDto(6, 9, false));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(5, coordinateTargeted.getX());
        Assertions.assertEquals(8, coordinateTargeted.getY());

    }


    @Test
    void iaAttackCloudBorderTop() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(5, 0, true));
        grid.updateCell(createRevealedCellDto(4, 0, false));
        grid.updateCell(createRevealedCellDto(6, 0, false));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(5, coordinateTargeted.getX());
        Assertions.assertEquals(1, coordinateTargeted.getY());

    }


    @Test
    void iaAttackRandom() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(3, 3);
        grid.updateCell(createRevealedCellDto(0, 0, false));
        grid.updateCell(createRevealedCellDto(1, 0, false));
        grid.updateCell(createRevealedCellDto(2, 0, false));
        grid.updateCell(createRevealedCellDto(0, 1, false));
        grid.updateCell(createRevealedCellDto(0, 2, false));
        grid.updateCell(createRevealedCellDto(1, 2, false));
        grid.updateCell(createRevealedCellDto(2, 2, false));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertTrue(coordinateTargeted.getX() == 1 || coordinateTargeted.getX() == 2);
        Assertions.assertEquals(1, coordinateTargeted.getY());

    }

    @Test
    void iaAttackBoatIsolatedAndRandomBorder() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(5, 0, true));
        grid.updateCell(createRevealedCellDto(6, 0, false));
        grid.updateCell(createRevealedCellDto(4, 0, false));

        grid.updateCell(createRevealedCellDto(5, 1, true));
        grid.updateCell(createRevealedCellDto(6, 1, false));
        grid.updateCell(createRevealedCellDto(4, 1, false));

        grid.updateCell(createRevealedCellDto(5, 2, false));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertNotNull(coordinateTargeted);
        Assertions.assertTrue(coordinateTargeted.getX() >= 0 && coordinateTargeted.getX() < 10);
        Assertions.assertTrue(coordinateTargeted.getY() >= 0 && coordinateTargeted.getY() < 10);
    }


    @Test
    void iaAttackBoatIsolatedAndRandomInGrid() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

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


        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(7, coordinateTargeted.getX());
        Assertions.assertEquals(0, coordinateTargeted.getY());
    }


    @Test
    void iaAttackBoatAlone() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(0, 0, true));
        grid.updateCell(createRevealedCellDto(1, 0, false));
        grid.updateCell(createRevealedCellDto(0, 1, false));

        grid.updateCell(createRevealedCellDto(8, 2, true));
        grid.updateCell(createRevealedCellDto(8, 3, true));

        List<BoatType> boatsToFinds = new ArrayList<>();
        boatsToFinds.add(BoatType.TORPILLEUR);

        CoordinateDTO coordinateTargeted = ia.iaAttack(grid, boatsToFinds);

        Assertions.assertEquals(8, coordinateTargeted.getX());
        Assertions.assertEquals(4, coordinateTargeted.getY());

    }


    @Test
    void calculBestCoordToAttackFromCellXplus1() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 1, true));
        grid.updateCell(createRevealedCellDto(1, 0, false));
        grid.updateCell(createRevealedCellDto(1, 2, false));
        grid.updateCell(createRevealedCellDto(0, 1, false));


        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromOneUncoverBoatCell(grid, List.of(BoatType.SOUS_MARIN_1), createRevealedCellDto(1, 1, true));

        Assertions.assertEquals(2, coordinateTargted.getX());
        Assertions.assertEquals(1, coordinateTargted.getY());
    }


    @Test
    void calculBestCoordToAttackFromCellXMoins1() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 1, true));
        grid.updateCell(createRevealedCellDto(2, 1, false));
        grid.updateCell(createRevealedCellDto(1, 0, false));
        grid.updateCell(createRevealedCellDto(1, 2, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromOneUncoverBoatCell(grid, List.of(BoatType.TORPILLEUR), createRevealedCellDto(1, 1, true));

        Assertions.assertEquals(0, coordinateTargted.getX());
        Assertions.assertEquals(1, coordinateTargted.getY());
    }

    @Test
    void calculBestCoordToAttackFromOneUncoverBoatCellVertUp() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 1, true));
        grid.updateCell(createRevealedCellDto(2, 1, false));
        grid.updateCell(createRevealedCellDto(0, 1, false));
        grid.updateCell(createRevealedCellDto(1, 2, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromOneUncoverBoatCell(grid, List.of(BoatType.TORPILLEUR), createRevealedCellDto(1, 1, true));

        Assertions.assertEquals(1, coordinateTargted.getX());
        Assertions.assertEquals(0, coordinateTargted.getY());
    }


    @Test
    void calculBestCoordToAttackFromOneUncoverBoatCellLeft() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(5, 5, true));
        grid.updateCell(createRevealedCellDto(6, 5, false));
        grid.updateCell(createRevealedCellDto(5, 4, false));
        grid.updateCell(createRevealedCellDto(5, 6, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromOneUncoverBoatCell(grid, List.of(BoatType.PORTE_AVIONS), createRevealedCellDto(5, 5, true));

        Assertions.assertEquals(4, coordinateTargted.getX());
        Assertions.assertEquals(5, coordinateTargted.getY());
    }


    @Test
    void calculBestCoordToAttackFromCellYplus1() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 1, true));
        grid.updateCell(createRevealedCellDto(2, 1, false));
        grid.updateCell(createRevealedCellDto(0, 1, false));
        grid.updateCell(createRevealedCellDto(1, 0, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromOneUncoverBoatCell(grid, List.of(BoatType.SOUS_MARIN_1), createRevealedCellDto(1, 1, true));

        Assertions.assertEquals(1, coordinateTargted.getX());
        Assertions.assertEquals(2, coordinateTargted.getY());
    }


    @Test
    void calculBestCoordToAttackFromCellYMoins1() {


        GridService gridService = new GridServiceImpl();
        IaPlayerServiceImpl ia = new IaPlayerServiceImpl(gridService);

        GridDTO grid = generateEmptyGrid(10, 10);
        grid.updateCell(createRevealedCellDto(1, 1, true));
        grid.updateCell(createRevealedCellDto(2, 1, false));
        grid.updateCell(createRevealedCellDto(0, 1, false));
        grid.updateCell(createRevealedCellDto(1, 2, false));

        CoordinateDTO coordinateTargted = ia.calculBestCoordToAttackFromOneUncoverBoatCell(grid, List.of(BoatType.TORPILLEUR), createRevealedCellDto(1, 1, true));

        Assertions.assertEquals(1, coordinateTargted.getX());
        Assertions.assertEquals(0, coordinateTargted.getY());
    }


}
