package com.patinaud.batailleservice.service;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import com.patinaud.bataillemodel.dto.GridDTO;

import java.util.List;

public interface GridService {

    GridDTO generateEmptyGrid(int width, int height);

    boolean theBoatCanBePositionHere(BoatDTO boatToPosition, List<BoatDTO> alreadyPositionedBoats, GridDTO grid);

    boolean theBoatCanEnterInTheGrid(BoatDTO boat, GridDTO grid);

    boolean thePositionIsFreeOfOtherBoats(BoatDTO boat, List<BoatDTO> alreadyPositionedBoats);

    boolean atLeastOneBoatOccupiesTheCell(List<BoatDTO> boats, CoordinateDTO coordinateCell);

    boolean theBoatOccupiesTheCell(BoatDTO boat, CoordinateDTO coordinateCell);

    int countNumberOfRevealedCellWhichContainsABoatFromThisCoordinate(GridDTO grid, CoordinateDTO coordinate, int evolveX, int evolveY);


    int countNumberOfUnrevealedCellFromThisCoordinate(GridDTO grid, CoordinateDTO coordinate, int evolveX, int evolveY);


    int countNumberOfPositionTheBoatCanTakeInTheCoordinate(GridDTO grid, BoatType boatType, CoordinateDTO coordinate);

    int countNumberOfPositionTheBoatCanTakeInTheCoordinateWithDirection(GridDTO grid, BoatType boatType, CoordinateDTO coordinate, boolean horizontalDirection);

    int countNumberOfPositionBoatsCanTakeInTheCoordinate(GridDTO grid, List<BoatType> boatTypes, CoordinateDTO coordinate);

}
