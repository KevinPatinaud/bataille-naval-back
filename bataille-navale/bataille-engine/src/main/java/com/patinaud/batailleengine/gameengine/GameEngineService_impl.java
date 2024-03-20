package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.*;
import com.patinaud.bataillecommunication.communication.PlayerCommunicationService;
import com.patinaud.bataillepersistence.persistence.PersistenceService;
import com.patinaud.batailleplayer.ia.IaPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class GameEngineService_impl implements GameEngineService {
    @Autowired
    PlayerCommunicationService playerCommunicationService;

    @Autowired
    PersistenceService persistenceService;

    @Autowired
    IaPlayerService iaPlayerService;


    public GameDTO generateNewGame() {
        // Génère un id unique
        String idGame = System.currentTimeMillis() + "W" + UUID.randomUUID().toString();

        // initialise le jeu
        persistenceService.initializeGame(idGame);

        positionIaPlayerBoats(idGame);

        GameDTO gameDTO = new GameDTO();
        gameDTO.setIdGame(idGame);

        return gameDTO;
    }


    private void positionIaPlayerBoats(String idGame) {

        ArrayList<BoatType> boatsToPosition = new ArrayList<>();
        boatsToPosition.add(BoatType.PORTE_AVIONS);
        boatsToPosition.add(BoatType.CROISEUR);
        boatsToPosition.add(BoatType.SOUS_MARIN_1);
        boatsToPosition.add(BoatType.SOUS_MARIN_2);
        boatsToPosition.add(BoatType.TORPILLEUR);

        persistenceService.setBoatPosition(idGame, IdPlayer.PLAYER_2, iaPlayerService.positionBoatOnGrid(boatsToPosition, 10, 10));

    }

    @Override
    public void positionHumanPlayerBoat(String idGame, ArrayList<BoatDTO> boats) {
        persistenceService.setBoatPosition(idGame, IdPlayer.PLAYER_1, boats);
    }

    @Override
    public void playerAttack(String idGame, String idPlayerAttacker_in, int xTargeted, int yTargeted) {
        try {
            IdPlayer idPlayerAttacker = IdPlayer.valueOf(idPlayerAttacker_in.toUpperCase());
            IdPlayer idPlayerOpponent = getIdOpponent(idPlayerAttacker);


            if (!idPlayerAttacker.equals(getIdNextPlayerToPlay(idGame))) {
                System.out.println("for the game " + idGame + " player " + getIdNextPlayerToPlay(idGame) + " should play and not " + idPlayerAttacker);
                return;
            }


            revealeCell(idGame, idPlayerAttacker, idPlayerOpponent, xTargeted, yTargeted);


            iaPlay(idGame, idPlayerOpponent, idPlayerAttacker);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void diffuseInformationAboutPlayer(String idGame, IdPlayer idPlayer) {
        playerCommunicationService.diffuseRevealedCells(idGame, idPlayer, persistenceService.getRevealedCells(idGame, idPlayer), persistenceService.getBoats(idGame, idPlayer));
        playerCommunicationService.diffuseBoatsStates(idGame, idPlayer, persistenceService.getBoats(idGame, idPlayer));
    }

    public void diffuseEndGameScore(String idGame, IdPlayer winner, IdPlayer looser) {
        EndGameResultDTO endGameResult = new EndGameResultDTO();
        endGameResult.setIdPlayerWin(winner);
        endGameResult.setIdPlayerLose(looser);
        playerCommunicationService.diffuseEndGame(idGame, endGameResult);
    }

    private IdPlayer getIdNextPlayerToPlay(String idGame) {
        return IdPlayer.PLAYER_1;
    }

    private IdPlayer getIdOpponent(IdPlayer idPlayer) {
        return idPlayer.equals(IdPlayer.PLAYER_1) ? IdPlayer.PLAYER_2 : IdPlayer.PLAYER_1;
    }

    private void revealeCell(String idGame, IdPlayer idPlayerAttacker, IdPlayer idPlayerTargeted, int xCellTargeted, int yCellTargeted) {
        persistenceService.revealCell(idGame, idPlayerTargeted, xCellTargeted, yCellTargeted);
        persistenceService.updateStateBoats(idGame, idPlayerTargeted);
        persistenceService.revealCellsNextToDestroyedBoat(idGame, idPlayerTargeted);

        diffuseInformationAboutPlayer(idGame, idPlayerTargeted);

        if (isAllBoatDestroyed(idGame, idPlayerTargeted)) {
            diffuseEndGameScore(idGame, idPlayerAttacker, idPlayerTargeted);
        }

    }

    private boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {
        return persistenceService.isAllBoatDestroyed(idGame, idPlayer);
    }


    private void iaPlay(String idGame, IdPlayer idIaPlayer, IdPlayer idPlayerTargeted) {
        ArrayList<CellDTO> cells = persistenceService.getRevealedCells(idGame, idPlayerTargeted);
        CoordinateDTO coordinateToReveal = iaPlayerService.iaAttack(cells, 10, 10);
        revealeCell(idGame, idIaPlayer, idPlayerTargeted, coordinateToReveal.getX(), coordinateToReveal.getY());

    }

}
