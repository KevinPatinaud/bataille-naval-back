package com.patinaud.bataillecommunication.communication;

import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;
import com.patinaud.bataillemodel.dto.GameDTO;
import com.patinaud.bataillemodel.dto.GridDTO;

import java.util.List;

public interface PlayerCommunicationService {

    void playerJoinTheGameEvent(String idGame, IdPlayer idplayer);

    void playerPositionBoatsEvent(String idGame, IdPlayer idplayer);


    void endGameEvent(String idGame, EndGameResultDTO endGameResult);

    void diffuseGameState(GameDTO game, GridDTO gridPlayer1, List<BoatDTO> boatsPlayer1, GridDTO gridPlayer2, List<BoatDTO> boatsPlayer2);
}
