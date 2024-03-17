package com.patinaud.batailleplayer.ia;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

            if (!cellIsFree(alreadyPositionnedBoats, x, y)) {
                allCellsAreFree = false;
            }
        }

        return allCellsAreFree;
    }

    public boolean cellIsFree(List<BoatDTO> alreadyPositionnedBoats, int xCell, int yCell) {
        boolean cellIsFree = true;

        for (int i = 0; i < alreadyPositionnedBoats.size(); i++) {
            if (boatOccupiesTheCell(alreadyPositionnedBoats.get(i), xCell, yCell)) {
                cellIsFree = false;
            }
        }

        return cellIsFree;
    }

    public boolean boatOccupiesTheCell(BoatDTO boat, int xCell, int yCell) {
        int xMin = boat.getxHead() - 1;
        int yMin = boat.getyHead() - 1;
        int xMax = boat.getxHead() + (boat.isHorizontal() ? boat.getBoatType().getSize() : 1);
        int yMax = boat.getyHead() + (!boat.isHorizontal() ? boat.getBoatType().getSize() : 1);

        return xCell >= xMin && xCell <= xMax && yCell >= yMin && yCell <= yMax;
    }

}
