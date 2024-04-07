package com.patinaud.bataillecommunication.communication;

import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;
import com.patinaud.bataillemodel.dto.GridDTO;

import java.util.List;

public interface PlayerCommunicationService {

    void diffuseGrid(String idGame, IdPlayer idplayer, GridDTO grid);

    void diffuseEndGame(String idGame, EndGameResultDTO endGameResult);

    void diffuseBoats(String idGame, IdPlayer idPlayer, List<BoatDTO> boats);
}
