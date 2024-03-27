package com.patinaud.batailleplayer.ia;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.CoordinateDTO;
import com.patinaud.bataillemodel.dto.GridDTO;

import java.util.ArrayList;
import java.util.List;

public interface IaPlayerService {
    public ArrayList<BoatDTO> positionBoatOnGrid(List<BoatType> boats, GridDTO grid);

    public CoordinateDTO iaAttack(GridDTO grid, List<BoatType> boatsToFinds);
}
