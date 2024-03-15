package com.patinaud.batailleservice.service.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.patinaud.bataillemodel.constants.GameAction;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.PlayerCells;
import com.patinaud.batailleservice.service.responseobj.EndGameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;


@Service
public class PlayerCommunicationService_impl implements PlayerCommunicationService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void diffuseRevealedCells(String idGame, PlayerCells playerCells) {
        this.messagingTemplate.convertAndSend("/diffuse/" + idGame + "/" + GameAction.REVEALED_CELLS.getValue(),
                gson.toJson(playerCells)
        );
    }

    @Override
    public void diffuseEndGame(String idGame, EndGameResult endGameResult) {
        this.messagingTemplate.convertAndSend("/diffuse/" + idGame + "/" + GameAction.END_GAME.getValue(),
                gson.toJson(endGameResult)
        );
    }
}
