package com.patinaud.batailleservice.service.gameengine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.GameAction;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.PlayerCells;
import com.patinaud.batailleservice.service.communication.PlayerCommunicationService;
import com.patinaud.batailleservice.service.persistence.PersistenceService;
import com.patinaud.batailleservice.service.responseobj.EndGameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameEngineService_impl implements GameEngineService {
    @Autowired
    PlayerCommunicationService playerCommunicationService;

    @Autowired
    PersistenceService persistenceService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String generateNewGame() {
        // Génère un id unique
        String idGame = System.currentTimeMillis() + "W" + UUID.randomUUID().toString();
        System.out.println("idGame genrated : " + idGame);

        // initialise le jeu

        // renvois l'id unique de la partie
        return idGame;
    }

    @Override
    public void playerAttack(String idGame, String idPlayerAttacker_in, int xTargeted, int yTargeted) {

        IdPlayer idPlayerAttacker = null;
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


        // communique les cellules adverses qui sont révélées
        playerCommunicationService.diffuseRevealedCells(idGame, getPlayerRevealedCells(idGame, idPlayerOpponent));


        // Si tous les bateaux du joueur adversaire sont détruits
        if (isAllBoatDestroyed(idGame, idPlayerOpponent)) {
            EndGameResult endGameResult = new EndGameResult();
            endGameResult.setIdPlayerWin(idPlayerAttacker);
            endGameResult.setIdPlayerLose(idPlayerOpponent);
            // met fin à la partie et communique le gagnant
            playerCommunicationService.diffuseEndGame(idGame, endGameResult);
        }


        // Indique que c'est à l'autre joueur de jouer

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


    private PlayerCells getPlayerRevealedCells(String idGame, IdPlayer idPlayer) {

        PlayerCells playerCells = new PlayerCells();

        playerCells.setIdPlayer(idPlayer);
        playerCells.setCells(persistenceService.getRevealedCells(idGame, idPlayer));

        return playerCells;
    }

    private boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {
        return persistenceService.isAllBoatDestroyed(idGame, idPlayer);
    }

}
