package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillecommunication.communication.PlayerCommunicationService;
import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.GameMode;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.*;
import com.patinaud.bataillepersistence.persistence.PersistenceGameService;
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
    PersistenceGameService persistenceGameService;
    IaPlayerService iaPlayerService;
    GridService gridService;
    @Value("${ID_GAME_WORDS}")
    private List<String> idGameWords;


    @Autowired
    public GameEngineServiceImpl(PlayerCommunicationService playerCommunicationService, PersistenceGameService persistenceGameService, IaPlayerService iaPlayerService, GridService gridService) {
        this.playerCommunicationService = playerCommunicationService;
        this.persistenceGameService = persistenceGameService;
        this.iaPlayerService = iaPlayerService;
        this.gridService = gridService;

    }

    public boolean isValidIdGame(String idGame) {
        return idGame != null && idGame.length() < 50 && idGamePattern.matcher(idGame).matches() && persistenceGameService.isGameExist(idGame);
    }

    public String generateIdGame() {
        String idGame;
        do {
            idGame = idGameWords.get(random.nextInt(idGameWords.size())) + "_" + String.format("%04d", random.nextInt(10000));
        } while (persistenceGameService.isGameExist(idGame));

        return idGame;
    }

    public boolean playerJoinGame(String idGame) {
        if (!isValidIdGame(idGame)) {
            return false;
        }
        playerCommunicationService.playerJoinTheGameEvent(idGame, IdPlayer.PLAYER_2);
        return true;
    }

    public GameDTO generateNewGame(GameMode gameMode) throws Exception {

        String idGame = generateIdGame();


        GameDTO game = new GameDTO();
        game.setId(idGame);
        game.setIdPlayerTurn(IdPlayer.PLAYER_1);
        game.setMode(gameMode);

        persistenceGameService.saveGame(game);


        PlayerDTO player1 = new PlayerDTO();
        player1.setIdPlayer(IdPlayer.PLAYER_1);
        player1.setGame(game);
        player1.setIA(false);
        persistenceGameService.savePlayer(player1);


        GridDTO gridPlayer1 = gridService.generateEmptyGrid(10, 10);

        persistenceGameService.saveGrid(idGame, player1.getIdPlayer(), gridPlayer1);


        PlayerDTO player2 = new PlayerDTO();
        player2.setIdPlayer(IdPlayer.PLAYER_2);
        player2.setGame(game);
        player2.setIA(true);
        persistenceGameService.savePlayer(player2);


        GridDTO gridPlayer2 = gridService.generateEmptyGrid(10, 10);

        persistenceGameService.saveGrid(idGame, player2.getIdPlayer(), gridPlayer2);

        if (GameMode.SOLO.equals(gameMode)) {
            positionIaPlayerBoats(idGame);
        }


        return game;
    }

    public boolean isGameWaitingForSecondPlayerJoin(String idGame) {
        // TODO completer les règles fonctionnelles !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return persistenceGameService.isGameExist(idGame);
    }


    private void positionIaPlayerBoats(String idGame) {

        ArrayList<BoatType> boatsToPosition = new ArrayList<>();
        boatsToPosition.add(BoatType.PORTE_AVIONS);
        boatsToPosition.add(BoatType.CROISEUR);
        boatsToPosition.add(BoatType.SOUS_MARIN_1);
        boatsToPosition.add(BoatType.SOUS_MARIN_2);
        boatsToPosition.add(BoatType.TORPILLEUR);

        positionPlayerBoats(idGame, IdPlayer.PLAYER_2, iaPlayerService.positionBoatOnGrid(boatsToPosition, gridService.generateEmptyGrid(10, 10)));
    }

    @Override
    public void positionPlayerBoats(String idGame, IdPlayer idPlayer, List<BoatDTO> boats) {
        persistenceGameService.setBoatPosition(idGame, idPlayer, boats);
        playerCommunicationService.playerPositionBoatsEvent(idGame, idPlayer);
    }


    @Override
    public void playerAttack(String idGame, IdPlayer idPlayerAttacker, CoordinateDTO coordinateTargeted) {

        IdPlayer idPlayerOpponent = getIdOpponent(idPlayerAttacker);

        persistenceGameService.updateIdPlayerTurn(idGame, idPlayerOpponent);
        revealCell(idGame, idPlayerAttacker, idPlayerOpponent, coordinateTargeted);


        if (persistenceGameService.getGameMode(idGame).equals(GameMode.SOLO)) {
            persistenceGameService.updateIdPlayerTurn(idGame, idPlayerAttacker);
            iaPlay(idGame, idPlayerOpponent, idPlayerAttacker);
        }

    }


    private void diffuseGameState(String idGame) {

        GameDTO game = persistenceGameService.getGame(idGame);

        GridDTO gridPlayer1 = persistenceGameService.getGrid(idGame, IdPlayer.PLAYER_1);
        List<BoatDTO> boatsPlayer1 = persistenceGameService.getBoats(idGame, IdPlayer.PLAYER_1);

        GridDTO gridPlayer2 = persistenceGameService.getGrid(idGame, IdPlayer.PLAYER_2);
        List<BoatDTO> boatsPlayer2 = persistenceGameService.getBoats(idGame, IdPlayer.PLAYER_2);

        playerCommunicationService.diffuseGameState(game, gridPlayer1, boatsPlayer1, gridPlayer2, boatsPlayer2);

    }

    public void diffuseEndGameScore(String idGame, IdPlayer winner, IdPlayer looser) {
        EndGameResultDTO endGameResult = new EndGameResultDTO();
        endGameResult.setIdPlayerWin(winner);
        endGameResult.setIdPlayerLose(looser);
        playerCommunicationService.endGameEvent(idGame, endGameResult);
    }


    public IdPlayer getIdOpponent(IdPlayer idPlayer) {
        return idPlayer.equals(IdPlayer.PLAYER_1) ? IdPlayer.PLAYER_2 : IdPlayer.PLAYER_1;
    }

    private void revealCell(String idGame, IdPlayer idPlayerAttacker, IdPlayer idPlayerTargeted, CoordinateDTO coordinateTargeted) {

        persistenceGameService.revealCell(idGame, idPlayerTargeted, coordinateTargeted);
        persistenceGameService.updateStateBoats(idGame, idPlayerTargeted);
        persistenceGameService.revealCellsNextToDestroyedBoat(idGame, idPlayerTargeted);

        diffuseGameState(idGame);

        if (isAllBoatDestroyed(idGame, idPlayerTargeted)) {
            diffuseEndGameScore(idGame, idPlayerAttacker, idPlayerTargeted);
        }

    }

    private boolean isAllBoatDestroyed(String idGame, IdPlayer idPlayer) {
        return persistenceGameService.isAllBoatDestroyed(idGame, idPlayer);
    }


    private void iaPlay(String idGame, IdPlayer idIaPlayer, IdPlayer idPlayerTargeted) {
        GridDTO grid = persistenceGameService.getGrid(idGame, idPlayerTargeted);

        List<BoatType> boatsToFinds = persistenceGameService.getBoats(idGame, idPlayerTargeted).stream().filter(boat -> !boat.isDestroyed()).map(BoatDTO::getBoatType).toList();

        CoordinateDTO coordinateToReveal = iaPlayerService.iaAttack(grid, boatsToFinds);
        revealCell(idGame, idIaPlayer, idPlayerTargeted, coordinateToReveal);

    }

}
