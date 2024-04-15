package com.patinaud.bataillecommunication.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.patinaud.bataillecommunication.mapper.EndGameResultMapper;
import com.patinaud.bataillecommunication.mapper.GameMapper;
import com.patinaud.bataillemodel.constants.GameAction;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;
import com.patinaud.bataillemodel.dto.GameDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PlayerCommunicationServiceImpl implements PlayerCommunicationService {

    private static final String DIFFUSE_PATH = "/diffuse/";


    private final SimpMessageSendingOperations messageSendingOperations;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    public PlayerCommunicationServiceImpl(SimpMessageSendingOperations messageSendingOperations) {
        this.messageSendingOperations = messageSendingOperations;
    }

    @Override
    public void playerJoinTheGameEvent(String idGame, IdPlayer idplayer) {
        this.messageSendingOperations.convertAndSend(DIFFUSE_PATH + idGame + "/" + GameAction.PLAYER_JOIN.getValue(), gson.toJson(idplayer));
    }

    @Override
    public void playerPositionBoatsEvent(String idGame, IdPlayer idplayer) {
        this.messageSendingOperations.convertAndSend(DIFFUSE_PATH + idGame + "/" + GameAction.PLAYER_POSITION_BOAT.getValue(), gson.toJson(idplayer));
    }

    @Override
    public void diffuseGameState(GameDTO game, GridDTO gridPlayer1, List<BoatDTO> boatsPlayer1, GridDTO gridPlayer2, List<BoatDTO> boatsPlayer2) {
        this.messageSendingOperations.convertAndSend(DIFFUSE_PATH + game.getId() + "/" + GameAction.GAME_STATE.getValue(),
                gson.toJson(
                        GameMapper.toResponse(game, gridPlayer1, boatsPlayer1, gridPlayer2, boatsPlayer2)
                )
        );
    }

    @Override
    public void endGameEvent(String idGame, EndGameResultDTO endGameResult) {
        this.messageSendingOperations.convertAndSend(DIFFUSE_PATH + idGame + "/" + GameAction.END_GAME.getValue(),
                gson.toJson(
                        EndGameResultMapper.fromDtoToResponse(endGameResult)
                )
        );
    }


}
