package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;
import com.patinaud.bataillecommunication.communication.PlayerCommunicationService;
import com.patinaud.bataillepersistence.persistence.PersistenceService;
import com.patinaud.batailleplayer.ia.IaPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameEngineService_impl implements GameEngineService {
    @Autowired
    PlayerCommunicationService playerCommunicationService;

    @Autowired
    PersistenceService persistenceService;

    @Autowired
    IaPlayerService iaPlayerService;


    public String generateNewGame() {
        // Génère un id unique
        String idGame = System.currentTimeMillis() + "W" + UUID.randomUUID().toString();

        // initialise le jeu
        persistenceService.initializeGame(idGame);

        positionIaBoat(idGame);

        return idGame;
    }


    private void positionIaBoat(String idGame) {

        ArrayList<BoatType> boatsToPosition = new ArrayList<>();
        boatsToPosition.add(BoatType.PORTE_AVIONS);
        boatsToPosition.add(BoatType.TORPILLEUR);
        boatsToPosition.add(BoatType.CROISEUR);
        boatsToPosition.add(BoatType.SOUS_MARIN_1);
        boatsToPosition.add(BoatType.SOUS_MARIN_2);

        persistenceService.setBoatPosition(idGame, IdPlayer.PLAYER_2, iaPlayerService.positionBoatOnGrid(boatsToPosition, 10, 10));

    }

    @Override
    public void playerAttack(String idGame, String idPlayerAttacker_in, int xTargeted, int yTargeted) {

        IdPlayer idPlayerAttacker;
        try {
            idPlayerAttacker = IdPlayer.valueOf(idPlayerAttacker_in.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        IdPlayer idPlayerOpponent = getIdOpponent(idPlayerAttacker);


        // vérifier que le joueur qui envoi l'attaque doit bien jouer
        if (!idPlayerAttacker.equals(getIdNextPlayerToPlay(idGame))) {
            System.out.println("for the game " + idGame + " player " + getIdNextPlayerToPlay(idGame) + " should play and not " + idPlayerAttacker);
            return;
        }

        // Révèle la cellule sur la grille adverse
        revealeCell(idGame, idPlayerOpponent, xTargeted, yTargeted);


        // communique les cellules et l'états des bateaux adverses
        diffuseInformationAboutPlayer(idGame, idPlayerOpponent);

        // Si tous les bateaux du joueur adversaire sont détruits
        if (isAllBoatDestroyed(idGame, idPlayerOpponent)) {
            EndGameResultDTO endGameResult = new EndGameResultDTO();
            endGameResult.setIdPlayerWin(idPlayerAttacker);
            endGameResult.setIdPlayerLose(idPlayerOpponent);
            // met fin à la partie et communique le gagnant
            playerCommunicationService.diffuseEndGame(idGame, endGameResult);
        }


        // Indique que c'est à l'autre joueur de jouer

    }

    private void diffuseInformationAboutPlayer(String idGame, IdPlayer idPlayer) {
        playerCommunicationService.diffuseRevealedCells(idGame, idPlayer, persistenceService.getRevealedCells(idGame, idPlayer), persistenceService.getBoats(idGame, idPlayer));
        playerCommunicationService.diffuseBoatsStates(idGame, idPlayer, persistenceService.getBoats(idGame, idPlayer));

    }

    private IdPlayer getIdNextPlayerToPlay(String idGame) {
        return IdPlayer.PLAYER_1;
    }

    private IdPlayer getIdOpponent(IdPlayer idPlayer) {
        return idPlayer.equals(IdPlayer.PLAYER_1) ? IdPlayer.PLAYER_2 : IdPlayer.PLAYER_1;
    }

    private CellContent revealeCell(String idGame, IdPlayer idPlayerTargeted, int xCellTargeted, int yCellTargeted) {
        CellContent cellContent = persistenceService.revealeCell(idGame, idPlayerTargeted, xCellTargeted, yCellTargeted);
        persistenceService.updateStateBoats(idGame, idPlayerTargeted);

        return cellContent;
    }


    private boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {
        return persistenceService.isAllBoatDestroyed(idGame, idPlayer);
    }

}
