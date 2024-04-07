package com.patinaud.bataillecommunication.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.patinaud.bataillecommunication.mapper.EndGameResultMapper;
import com.patinaud.bataillecommunication.mapper.PlayerBoatsStatesMapper;
import com.patinaud.bataillecommunication.mapper.PlayerCellsMapper;
import com.patinaud.bataillemodel.constants.GameAction;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlayerCommunicationServiceImpl implements PlayerCommunicationService {

    private static final String DIFFUSE_PATH = "/diffuse/";


    private final SimpMessageSendingOperations messagingTemplate;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    public PlayerCommunicationServiceImpl(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @Override
    public void diffuseGrid(String idGame, IdPlayer idplayer, GridDTO grid) {
        this.messagingTemplate.convertAndSend(DIFFUSE_PATH + idGame + "/" + GameAction.GRID.getValue(),
                gson.toJson(
                        PlayerCellsMapper.fromDtosToResponses(idplayer, grid.getCells().stream().flatMap(List::stream).collect(Collectors.toList()))
                )
        );
    }

    @Override
    public void diffuseEndGame(String idGame, EndGameResultDTO endGameResult) {
        this.messagingTemplate.convertAndSend(DIFFUSE_PATH + idGame + "/" + GameAction.END_GAME.getValue(),
                gson.toJson(
                        EndGameResultMapper.fromDtoToResponse(endGameResult)
                )
        );
    }

    @Override
    public void diffuseBoats(String idGame, IdPlayer idPlayer, List<BoatDTO> boats) {
        this.messagingTemplate.convertAndSend(DIFFUSE_PATH + idGame + "/" + GameAction.BOATS.getValue(),
                gson.toJson(
                        PlayerBoatsStatesMapper.fromDtoToResponse(idPlayer, boats)
                )
        );
    }
}
