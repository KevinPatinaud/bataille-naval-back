package com.patinaud.batailleplayer.ia;


import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IaPlayerServiceTest {

    @Test
    void allBoatArePositionned() {
        IaPlayerService_impl ia = new IaPlayerService_impl();

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
        IaPlayerService_impl ia = new IaPlayerService_impl();
        Assertions.assertTrue(ia.theBoatIsPositionnedInsideTheGrid(BoatType.CROISEUR, 1, 1, false, 10, 10));
        Assertions.assertTrue(ia.theBoatIsPositionnedInsideTheGrid(BoatType.CROISEUR, 0, 0, false, 10, 10));

        Assertions.assertFalse(ia.theBoatIsPositionnedInsideTheGrid(BoatType.CROISEUR, -1, 1, false, 10, 10));
        Assertions.assertFalse(ia.theBoatIsPositionnedInsideTheGrid(BoatType.CROISEUR, 0, -1, false, 10, 10));
        Assertions.assertFalse(ia.theBoatIsPositionnedInsideTheGrid(BoatType.CROISEUR, 10, 1, false, 10, 10));
        Assertions.assertFalse(ia.theBoatIsPositionnedInsideTheGrid(BoatType.CROISEUR, 0, 10, false, 10, 10));

    }


    @Test
    void theBoatIsPositionnedInsideTheGridHoriz() {
        IaPlayerService_impl ia = new IaPlayerService_impl();
        Assertions.assertTrue(ia.theBoatIsPositionnedInsideTheGrid(BoatType.PORTE_AVIONS, 2, 3, true, 10, 10));
    }


    @Test
    void theBoatIsNotPositionnedInsideTheGridHoriz() {
        IaPlayerService_impl ia = new IaPlayerService_impl();
        Assertions.assertFalse(ia.theBoatIsPositionnedInsideTheGrid(BoatType.PORTE_AVIONS, 9, 3, true, 10, 10));
    }


    @Test
    void theBoatIsPositionnedInsideTheGridVertical() {
        IaPlayerService_impl ia = new IaPlayerService_impl();
        Assertions.assertTrue(ia.theBoatIsPositionnedInsideTheGrid(BoatType.PORTE_AVIONS, 2, 3, false, 10, 10));
    }


    @Test
    void theBoatIsNotPositionnedInsideTheGridVertical() {
        IaPlayerService_impl ia = new IaPlayerService_impl();
        Assertions.assertFalse(ia.theBoatIsPositionnedInsideTheGrid(BoatType.PORTE_AVIONS, 4, 8, false, 10, 10));
    }


    @Test
    void boatOccupiesTheCellHoriz() {
        IaPlayerService_impl ia = new IaPlayerService_impl();
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
        IaPlayerService_impl ia = new IaPlayerService_impl();
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
    void thePositionIsNotFree() {

        IaPlayerService_impl ia = new IaPlayerService_impl();
        BoatDTO boat = new BoatDTO();
        boat.setBoatType(BoatType.TORPILLEUR);
        boat.setxHead(5);
        boat.setyHead(5);
        boat.setHorizontal(false);

        ArrayList<BoatDTO> boatsAlreadyPositionned = new ArrayList<>();
        boatsAlreadyPositionned.add(boat);

        Assertions.assertFalse(ia.thePositionIsFree(BoatType.PORTE_AVIONS, boatsAlreadyPositionned, 4, 6, false));
    }

}
