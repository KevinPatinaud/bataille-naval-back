package com.patinaud.batailleservice.service;

import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import com.patinaud.bataillemodel.dto.GridDTO;

import java.util.List;

public interface GridService {

    public GridDTO generateEmptyGrid(int width, int height);

    public boolean theBoatCanBePositionHere(BoatDTO boatToPosition, List<BoatDTO> alreadyPositionedBoats, GridDTO grid );

    public boolean theBoatCanEnterInTheGrid(BoatDTO boat, GridDTO grid);

    public boolean thePositionIsFreeOfOtherBoats(BoatDTO boat, List<BoatDTO> alreadyPositionedBoats);

    public boolean atLeastOneBoatOccupiesTheCell(List<BoatDTO> boats, CoordinateDTO coordinateCell);

    public boolean theBoatOccupiesTheCell(BoatDTO boat, CoordinateDTO coordinateCell);

}
