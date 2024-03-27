package com.patinaud.batailleservice.service;

import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GridServiceImpl implements GridService {

    public GridDTO generateEmptyGrid(int width, int height) {

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


    public boolean theBoatCanBePositionHere(BoatDTO boatToPosition, List<BoatDTO> alreadyPositionedBoats, GridDTO grid) {
        return theBoatCanEnterInTheGrid(boatToPosition, grid) && thePositionIsFreeOfOtherBoats(boatToPosition, alreadyPositionedBoats);
    }

    public boolean theBoatCanEnterInTheGrid(BoatDTO boat, GridDTO grid) {

        if (boat.getxHead() >= 0 && boat.getxHead() < grid.getWidth() && boat.getyHead() >= 0 && boat.getyHead() < grid.getHeight()) {
            if (boat.isHorizontal() && boat.getxHead() + boat.getBoatType().getSize() < grid.getWidth()) {
                return true;
            }
            return !boat.isHorizontal() && boat.getyHead() + boat.getBoatType().getSize() < grid.getHeight();
        }
        return false;
    }

    public boolean thePositionIsFreeOfOtherBoats(BoatDTO boat, List<BoatDTO> alreadyPositionedBoats) {

        for (int i = 0; i < boat.getBoatType().getSize(); i++) {
            int x = boat.getxHead() + (boat.isHorizontal() ? i : 0);
            int y = boat.getyHead() + (!boat.isHorizontal() ? i : 0);

            if (atLeastOneBoatOccupiesTheCell(alreadyPositionedBoats, new CoordinateDTO(x, y))) {
                return false;
            }
        }

        return true;
    }

    public boolean atLeastOneBoatOccupiesTheCell(List<BoatDTO> boats, CoordinateDTO coordinateCell) {
        if (boats == null) return false;

        boolean cellContainsABoat = false;

        for (int i = 0; i < boats.size(); i++) {
            if (theBoatOccupiesTheCell(boats.get(i), coordinateCell)) {
                cellContainsABoat = true;
            }
        }

        return cellContainsABoat;
    }

    public boolean theBoatOccupiesTheCell(BoatDTO boat, CoordinateDTO coordinateCell) {
        int xMin = boat.getxHead() - 1;
        int yMin = boat.getyHead() - 1;
        int xMax = boat.getxHead() + (boat.isHorizontal() ? boat.getBoatType().getSize() : 1);
        int yMax = boat.getyHead() + (!boat.isHorizontal() ? boat.getBoatType().getSize() : 1);

        return coordinateCell.getX() >= xMin && coordinateCell.getX() <= xMax && coordinateCell.getY() >= yMin && coordinateCell.getY() <= yMax;
    }


    public int countNumberOfRevealedCellWhichContainsABoatFromThisCoordinate(GridDTO grid, CoordinateDTO coordinate, int evolveX, int evolveY) {
        int inc = 1;
        int x = coordinate.getX() + evolveX * inc;
        int y = coordinate.getY() + evolveY * inc;

        while (
                grid.isInTheGrid(x, y) &&
                        grid.getCell(x, y).isRevealed() &&
                        grid.getCell(x, y).isOccupied()) {
            inc++;
            x = coordinate.getX() + evolveX * inc;
            y = coordinate.getY() + evolveY * inc;
        }
        return inc - 1;
    }


    public int countNumberOfUnrevealedCellFromThisCoordinate(GridDTO grid, CoordinateDTO coordinate, int evolveX, int evolveY) {
        int inc = 1;
        int x = coordinate.getX() + evolveX * inc;
        int y = coordinate.getY() + evolveY * inc;

        while (grid.isInTheGrid(x, y) && !grid.getCell(x, y).isRevealed()) {
            inc++;
            x = coordinate.getX() + evolveX * inc;
            y = coordinate.getY() + evolveY * inc;
        }
        return inc - 1;
    }

}
