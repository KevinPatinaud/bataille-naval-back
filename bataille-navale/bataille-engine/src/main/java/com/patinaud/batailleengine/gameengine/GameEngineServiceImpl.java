package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillecommunication.communication.PlayerCommunicationService;
import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.GameMode;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.*;
import com.patinaud.bataillepersistence.persistence.PersistenceService;
import com.patinaud.batailleplayer.ia.IaPlayerService;
import com.patinaud.batailleservice.service.GridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class GameEngineServiceImpl implements GameEngineService {

    private static final Pattern idGamePattern = Pattern.compile("^[A-Z0-9-]+_\\d{4}$");
    private final Random random = new Random();
    PlayerCommunicationService playerCommunicationService;
    PersistenceService persistenceService;
    IaPlayerService iaPlayerService;
    GridService gridService;
    @Value("${ID_GAME_WORDS}")
    private List<String> idGameWords;


    @Autowired
    public GameEngineServiceImpl(PlayerCommunicationService playerCommunicationService, PersistenceService persistenceService, IaPlayerService iaPlayerService, GridService gridService) {
        this.playerCommunicationService = playerCommunicationService;
        this.persistenceService = persistenceService;
        this.iaPlayerService = iaPlayerService;
        this.gridService = gridService;

    }

    public static boolean isValidIdGame(String idGame) {
        if (idGame == null || idGame.length() > 50) {
            return false;
        }

        return idGamePattern.matcher(idGame).matches();
    }

    public String generateIdGame() {
        String idGame;
        do {
            idGame = idGameWords.get(random.nextInt(idGameWords.size())) + "_" + String.format("%04d", random.nextInt(10000));
        } while (persistenceService.isGameExist(idGame));

        return idGame;
    }

    public GameDTO generateNewGame(String idGame, GameMode gameMode) throws Exception {

        if (!isValidIdGame(idGame) || persistenceService.isGameExist(idGame)) {
            throw new Exception("id game invalid");
        }


        GameDTO game = new GameDTO();
        game.setId(idGame);
        game.setIdPlayerTurn(IdPlayer.PLAYER_1);

        persistenceService.saveGame(game);


        PlayerDTO player1 = new PlayerDTO();
        player1.setIdPlayer(IdPlayer.PLAYER_1);
        player1.setGame(game);
        player1.setIA(false);
        persistenceService.savePlayer(player1);


        GridDTO gridPlayer1 = gridService.generateEmptyGrid(10, 10);

        persistenceService.saveGrid(idGame, player1.getIdPlayer(), gridPlayer1);


        PlayerDTO player2 = new PlayerDTO();
        player2.setIdPlayer(IdPlayer.PLAYER_2);
        player2.setGame(game);
        player2.setIA(true);
        persistenceService.savePlayer(player2);


        GridDTO gridPlayer2 = gridService.generateEmptyGrid(10, 10);

        persistenceService.saveGrid(idGame, player2.getIdPlayer(), gridPlayer2);


        positionIaPlayerBoats(idGame);

        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(idGame);

        return gameDTO;
    }


    private void positionIaPlayerBoats(String idGame) {

        ArrayList<BoatType> boatsToPosition = new ArrayList<>();
        boatsToPosition.add(BoatType.PORTE_AVIONS);
        boatsToPosition.add(BoatType.CROISEUR);
        boatsToPosition.add(BoatType.SOUS_MARIN_1);
        boatsToPosition.add(BoatType.SOUS_MARIN_2);
        boatsToPosition.add(BoatType.TORPILLEUR);

        persistenceService.setBoatPosition(idGame, IdPlayer.PLAYER_2, iaPlayerService.positionBoatOnGrid(boatsToPosition, gridService.generateEmptyGrid(10, 10)));

    }

    @Override
    public void positionHumanPlayerBoat(String idGame, IdPlayer idPlayer, List<BoatDTO> boats) {
        persistenceService.setBoatPosition(idGame, idPlayer, boats);
    }


    @Override
    public void playerAttack(String idGame, String idPlayerAttackerStr, CoordinateDTO coordinateTargeted) {

        IdPlayer idPlayerAttacker = IdPlayer.valueOf(idPlayerAttackerStr.toUpperCase());
        IdPlayer idPlayerOpponent = getIdOpponent(idPlayerAttacker);

        revealCell(idGame, idPlayerAttacker, idPlayerOpponent, coordinateTargeted);

        iaPlay(idGame, idPlayerOpponent, idPlayerAttacker);

    }


    private void diffuseInformationAboutPlayer(String idGame, IdPlayer idPlayer) {
        playerCommunicationService.diffuseGrid(idGame, idPlayer, persistenceService.getGrid(idGame, idPlayer));
        playerCommunicationService.diffuseBoats(idGame, idPlayer, persistenceService.getBoats(idGame, idPlayer));
    }

    public void diffuseEndGameScore(String idGame, IdPlayer winner, IdPlayer looser) {
        EndGameResultDTO endGameResult = new EndGameResultDTO();
        endGameResult.setIdPlayerWin(winner);
        endGameResult.setIdPlayerLose(looser);
        playerCommunicationService.diffuseEndGame(idGame, endGameResult);
    }


    public IdPlayer getIdOpponent(IdPlayer idPlayer) {
        return idPlayer.equals(IdPlayer.PLAYER_1) ? IdPlayer.PLAYER_2 : IdPlayer.PLAYER_1;
    }

    private void revealCell(String idGame, IdPlayer idPlayerAttacker, IdPlayer idPlayerTargeted, CoordinateDTO coordinateTargeted) {


        persistenceService.revealCell(idGame, idPlayerTargeted, coordinateTargeted);
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
        GridDTO grid = persistenceService.getGrid(idGame, idPlayerTargeted);

        List<BoatType> boatsToFinds = persistenceService.getBoats(idGame, idPlayerTargeted).stream().filter(boat -> !boat.isDestroyed()).map(BoatDTO::getBoatType).toList();

        CoordinateDTO coordinateToReveal = iaPlayerService.iaAttack(grid, boatsToFinds);
        revealCell(idGame, idIaPlayer, idPlayerTargeted, coordinateToReveal);

    }

}
