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
import java.util.List;
import java.util.UUID;

@Service
public class GameEngineServiceImpl implements GameEngineService {

    PlayerCommunicationService playerCommunicationService;
    PersistenceService persistenceService;
    IaPlayerService iaPlayerService;

    @Autowired
    public GameEngineServiceImpl(PlayerCommunicationService playerCommunicationService, PersistenceService persistenceService, IaPlayerService iaPlayerService) {
        this.playerCommunicationService = playerCommunicationService;
        this.persistenceService = persistenceService;
        this.iaPlayerService = iaPlayerService;

    }

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

    private GridDTO generateEmptyGrid(int width, int height) {

        List<List<CellDTO>> grid = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            List<CellDTO> line = new ArrayList<>();
            for (int y = 0; y < height; y++) {
                CellDTO cell = new CellDTO();
                cell.setX(x);
                cell.setY(y);
                cell.setRevealed(false);
                cell.setOccupied(false);
                line.add(cell);
            }
            grid.add(line);
        }
        GridDTO gridDto = new GridDTO();
        gridDto.setCells(grid);
        return gridDto;
    }

    private void positionIaPlayerBoats(String idGame) {

        ArrayList<BoatType> boatsToPosition = new ArrayList<>();
        boatsToPosition.add(BoatType.PORTE_AVIONS);
        boatsToPosition.add(BoatType.CROISEUR);
        boatsToPosition.add(BoatType.SOUS_MARIN_1);
        boatsToPosition.add(BoatType.SOUS_MARIN_2);
        boatsToPosition.add(BoatType.TORPILLEUR);

        persistenceService.setBoatPosition(idGame, IdPlayer.PLAYER_2, iaPlayerService.positionBoatOnGrid(boatsToPosition, generateEmptyGrid(10, 10)));

    }

    @Override
    public void positionHumanPlayerBoat(String idGame, ArrayList<BoatDTO> boats) {
        persistenceService.setBoatPosition(idGame, IdPlayer.PLAYER_1, boats);
    }


    @Override
    public void playerAttack(String idGame, String idPlayerAttackerStr, int xTargeted, int yTargeted) {
        try {

            IdPlayer idPlayerAttacker = IdPlayer.valueOf(idPlayerAttackerStr.toUpperCase());
            IdPlayer idPlayerOpponent = getIdOpponent(idPlayerAttacker);

            revealeCell(idGame, idPlayerAttacker, idPlayerOpponent, xTargeted, yTargeted);


            iaPlay(idGame, idPlayerOpponent, idPlayerAttacker);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void diffuseInformationAboutPlayer(String idGame, IdPlayer idPlayer) {
        playerCommunicationService.diffuseRevealedCells(idGame, idPlayer, persistenceService.getRevealedCells(idGame, idPlayer));
        playerCommunicationService.diffuseBoatsStates(idGame, idPlayer, persistenceService.getBoats(idGame, idPlayer));
    }

    public void diffuseEndGameScore(String idGame, IdPlayer winner, IdPlayer looser) {
        EndGameResultDTO endGameResult = new EndGameResultDTO();
        endGameResult.setIdPlayerWin(winner);
        endGameResult.setIdPlayerLose(looser);
        playerCommunicationService.diffuseEndGame(idGame, endGameResult);
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
