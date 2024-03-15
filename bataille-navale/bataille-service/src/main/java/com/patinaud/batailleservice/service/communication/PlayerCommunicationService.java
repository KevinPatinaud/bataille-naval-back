package com.patinaud.batailleservice.service.communication;

import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.PlayerCells;
import com.patinaud.batailleservice.service.responseobj.EndGameResult;

public interface PlayerCommunicationService {

    public void diffuseRevealedCells(String idGame, PlayerCells playerCells);

    public void diffuseEndGame(String idGame, EndGameResult endGameResult);
}
