package com.patinaud.batailleplayer.ia;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.dto.BoatDTO;

import java.util.ArrayList;
import java.util.List;

public interface IaPlayerService {
    public ArrayList<BoatDTO> positionBoatOnGrid(List<BoatType> boats, int widthGrid, int heigthGrid);
}
