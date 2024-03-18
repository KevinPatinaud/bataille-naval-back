package com.patinaud.bataillecommunication.communication;

import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;

import java.util.ArrayList;
import java.util.List;

public interface PlayerCommunicationService {

    public void diffuseRevealedCells(String idGame, IdPlayer idplayer, ArrayList<CellDTO> cells, ArrayList<BoatDTO> boats);

    public void diffuseEndGame(String idGame, EndGameResultDTO endGameResult);

    public void diffuseBoatsStates(String idGame, IdPlayer idPlayer, ArrayList<BoatDTO> boats);
}