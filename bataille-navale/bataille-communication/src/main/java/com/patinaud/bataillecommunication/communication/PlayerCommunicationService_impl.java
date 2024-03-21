package com.patinaud.bataillecommunication.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.patinaud.bataillecommunication.mapper.EndGameResultMapper;
import com.patinaud.bataillecommunication.mapper.PlayerBoatsStatesMapper;
import com.patinaud.bataillecommunication.mapper.PlayerCellsMapper;
import com.patinaud.bataillemodel.constants.GameAction;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class PlayerCommunicationService_impl implements PlayerCommunicationService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void diffuseRevealedCells(String idGame, IdPlayer idplayer, ArrayList<CellDTO> cells) {
        this.messagingTemplate.convertAndSend("/diffuse/" + idGame + "/" + GameAction.REVEALED_CELLS.getValue(),
                gson.toJson(
                        PlayerCellsMapper.fromDtosToResponses(idplayer, cells)
                )
        );
    }

    @Override
    public void diffuseEndGame(String idGame, EndGameResultDTO endGameResult) {
        this.messagingTemplate.convertAndSend("/diffuse/" + idGame + "/" + GameAction.END_GAME.getValue(),
                gson.toJson(
                        EndGameResultMapper.fromDtoToResponse(endGameResult)
                )
        );
    }

    @Override
    public void diffuseBoatsStates(String idGame, IdPlayer idPlayer, ArrayList<BoatDTO> boats) {
        this.messagingTemplate.convertAndSend("/diffuse/" + idGame + "/" + GameAction.BOATS_STATES.getValue(),
                gson.toJson(
                        PlayerBoatsStatesMapper.fromDtoToResponse(idPlayer, boats)
                )
        );
    }
}
