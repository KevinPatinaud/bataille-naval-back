package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;
import com.patinaud.bataillecommunication.communication.PlayerCommunicationService;
import com.patinaud.bataillemodel.dto.GameDTO;
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

        positionIaBoat(idGame);

        GameDTO gameDTO = new GameDTO();
        gameDTO.setIdGame(idGame);

        return gameDTO;
    }


    private void positionIaBoat(String idGame) {

        ArrayList<BoatType> boatsToPosition = new ArrayList<>();
        boatsToPosition.add(BoatType.PORTE_AVIONS);
        boatsToPosition.add(BoatType.CROISEUR);
        boatsToPosition.add(BoatType.SOUS_MARIN_1);
        boatsToPosition.add(BoatType.SOUS_MARIN_2);
        boatsToPosition.add(BoatType.TORPILLEUR);

        persistenceService.setBoatPosition(idGame, IdPlayer.PLAYER_2, iaPlayerService.positionBoatOnGrid(boatsToPosition, 10, 10));

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


            revealeCell(idGame, idPlayerOpponent, xTargeted, yTargeted);


            diffuseInformationAboutPlayer(idGame, idPlayerOpponent);


            if (isAllBoatDestroyed(idGame, idPlayerOpponent)) {
                diffuseEndGameScore(idGame, idPlayerAttacker, idPlayerOpponent);
            }

            // Indique que c'est à l'autre joueur de jouer

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
        // met fin à la partie et communique le gagnant
        playerCommunicationService.diffuseEndGame(idGame, endGameResult);
    }

    private IdPlayer getIdNextPlayerToPlay(String idGame) {
        return IdPlayer.PLAYER_1;
    }

    private IdPlayer getIdOpponent(IdPlayer idPlayer) {
        return idPlayer.equals(IdPlayer.PLAYER_1) ? IdPlayer.PLAYER_2 : IdPlayer.PLAYER_1;
    }

    private void revealeCell(String idGame, IdPlayer idPlayerTargeted, int xCellTargeted, int yCellTargeted) {
        persistenceService.revealCell(idGame, idPlayerTargeted, xCellTargeted, yCellTargeted);
        persistenceService.updateStateBoats(idGame, idPlayerTargeted);
        persistenceService.revealCellsNextToDestroyedBoat(idGame, idPlayerTargeted);
    }

    private boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {
        return persistenceService.isAllBoatDestroyed(idGame, idPlayer);
    }

}
