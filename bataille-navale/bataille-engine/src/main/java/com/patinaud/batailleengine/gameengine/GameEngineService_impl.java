package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.BoatDTO;
import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;
import com.patinaud.bataillecommunication.communication.PlayerCommunicationService;
import com.patinaud.bataillemodel.dto.GameDTO;
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


            // reveale all cells next to destroyed boat
            revealCellsNextToDestroyedBoat(idGame, idPlayerOpponent);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void revealeCell(String idGame, IdPlayer idPlayerTargeted, int xCellTargeted, int yCellTargeted) {
        persistenceService.revealeCell(idGame, idPlayerTargeted, xCellTargeted, yCellTargeted);
        persistenceService.updateStateBoats(idGame, idPlayerTargeted);

    }

    private void revealCellsNextToDestroyedBoat(String idGame, IdPlayer idPlayerTargeted) {

        ArrayList<BoatDTO> boats = persistenceService.getBoats(idGame, idPlayerTargeted);
        ArrayList<CellDTO> revealedCells = persistenceService.getRevealedCells(idGame, idPlayerTargeted);

        for (int iBoat = 0; iBoat < boats.size(); iBoat++) {
            BoatDTO boat = boats.get(iBoat);

            if (boat.isDestroyed()) {
                int xMin = boat.getxHead() - 1;
                int yMin = boat.getyHead() - 1;
                int xMax = boat.getxHead() + (boat.isHorizontal() ? boat.getBoatType().getSize() : 1);
                int yMax = boat.getyHead() + (!boat.isHorizontal() ? boat.getBoatType().getSize() : 1);

                for (int iX = xMin; iX <= xMax; iX++) {
                    for (int iY = yMin; iY <= yMax; iY++) {
                        boolean cellIsRevealed = false;
                        for (int iRc = 0; iRc < revealedCells.size(); iRc++) {
                            if (revealedCells.get(iRc).getX() == iX && revealedCells.get(iRc).getY() == iY) {
                                cellIsRevealed = true;
                            }
                        }
                        persistenceService.revealeCell(idGame, idPlayerTargeted, iX, iY);
                    }
                }
            }
        }

    }


    private boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {
        return persistenceService.isAllBoatDestroyed(idGame, idPlayer);
    }

}
