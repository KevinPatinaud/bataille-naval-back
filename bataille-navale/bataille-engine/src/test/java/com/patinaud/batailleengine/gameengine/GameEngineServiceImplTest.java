package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillecommunication.communication.PlayerCommunicationService;
import com.patinaud.batailleengine.config.TestConfiguration;
import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.GameMode;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.*;
import com.patinaud.bataillepersistence.persistence.PersistenceGameService;
import com.patinaud.batailleplayer.ia.IaPlayerService;
import com.patinaud.batailleservice.service.GridService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
@TestPropertySource(properties = {"ID_GAME_WORDS=AMIRAL, AMPHIBIE, ARSENAL"})
public class GameEngineServiceImplTest {

    @MockBean
    PersistenceGameService persistenceGameServiceMock;
    @MockBean
    IaPlayerService iaPlayerServiceMock;
    @MockBean
    GridService gridServiceMock;
    @MockBean
    PlayerCommunicationService playerCommunicationServiceMock;
    @Autowired
    private GameEngineServiceImpl gameEngineService;

    @Captor
    private ArgumentCaptor<GameDTO> argumentCaptorGameDTO;
    @Captor
    private ArgumentCaptor<PlayerDTO> argumentCaptorPlayerDTO;
    @Captor
    private ArgumentCaptor<CoordinateDTO> argumentCaptorCoordinateDTO;
    @Captor
    private ArgumentCaptor<EndGameResultDTO> argumentCaptorEndGameResultDTO;
    @Captor
    private ArgumentCaptor<List<BoatType>> argumentCaptorListBoatType;


    @Test
    public void generateNewGameTest() throws Exception {
        Mockito.doNothing().when(persistenceGameServiceMock).saveGame(any());
        Mockito.doNothing().when(persistenceGameServiceMock).savePlayer(any());
        Mockito.doNothing().when(persistenceGameServiceMock).saveGrid(any(), any(), any());
        Mockito.doNothing().when(persistenceGameServiceMock).setBoatPosition(any(), any(), any());


        Mockito.when(gridServiceMock.generateEmptyGrid(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new GridDTO());

        GameDTO gameDTO = gameEngineService.generateNewGame(GameMode.SOLO);

        assertFalse(gameDTO.getId().isEmpty());

        Mockito.verify(persistenceGameServiceMock).saveGame(argumentCaptorGameDTO.capture());
        assertEquals(gameDTO.getId(), argumentCaptorGameDTO.getValue().getId());

        Mockito.verify(persistenceGameServiceMock, Mockito.times(2)).savePlayer(argumentCaptorPlayerDTO.capture());
        assertEquals(IdPlayer.PLAYER_1, argumentCaptorPlayerDTO.getAllValues().get(0).getIdPlayer());
        assertEquals(IdPlayer.PLAYER_2, argumentCaptorPlayerDTO.getAllValues().get(1).getIdPlayer());

    }

    @Test
    void isValidIdGame() {
        Mockito.when(persistenceGameServiceMock.isGameExist(any())).thenReturn(true);
        assertTrue(gameEngineService.isValidIdGame("QUARTIER-MAITRE_3160"));
    }

    @Test
    void isNotAValidIdGameNotInDB() {
        Mockito.when(persistenceGameServiceMock.isGameExist(any())).thenReturn(false);
        assertFalse(gameEngineService.isValidIdGame("TORPILLE_4163"));
    }

    @Test
    void isNotAValidIdGameOversize() {
        Mockito.when(persistenceGameServiceMock.isGameExist(any())).thenReturn(true);
        assertFalse(gameEngineService.isValidIdGame("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA_3160"));
    }

    @Test
    void isNotAValidIdGameNull() {
        Mockito.when(persistenceGameServiceMock.isGameExist(any())).thenReturn(true);
        assertFalse(gameEngineService.isValidIdGame(null));
    }

    @Test
    void isNotAValidIdGameWrongFormat() {
        Mockito.when(persistenceGameServiceMock.isGameExist(any())).thenReturn(true);
        assertFalse(gameEngineService.isValidIdGame("ABCD_123_456"));
    }

    @Test
    void positionHumanPlayerBoatTest() {
        Mockito.doNothing().when(persistenceGameServiceMock).setBoatPosition(any(), any(), any());

        List<BoatDTO> boats = new ArrayList<>();

        gameEngineService.positionPlayerBoats("ABC", IdPlayer.PLAYER_1, boats);

        Mockito.verify(persistenceGameServiceMock).setBoatPosition("ABC", IdPlayer.PLAYER_1, boats);

    }

    private List<BoatDTO> generateListBoatDTO() {
        BoatDTO boatNotDestroyed = new BoatDTO();

        boatNotDestroyed.setBoatType(BoatType.PORTE_AVIONS);
        boatNotDestroyed.setxHead(2);
        boatNotDestroyed.setyHead(2);
        boatNotDestroyed.setHorizontal(false);
        boatNotDestroyed.setDestroyed(false);


        BoatDTO boatDestroyed = new BoatDTO();

        boatDestroyed.setBoatType(BoatType.TORPILLEUR);
        boatDestroyed.setxHead(4);
        boatDestroyed.setyHead(4);
        boatDestroyed.setHorizontal(false);
        boatDestroyed.setDestroyed(true);

        return List.of(boatNotDestroyed, boatDestroyed);
    }

    @Test
    void playerAttackSoloWithIAResponseTest() {
        Mockito.doNothing().when(persistenceGameServiceMock).revealCell(any(), any(), any());
        Mockito.doNothing().when(persistenceGameServiceMock).updateStateBoats(any(), any());
        Mockito.doNothing().when(persistenceGameServiceMock).revealCellsNextToDestroyedBoat(any(), any());
        Mockito.when(persistenceGameServiceMock.getGameMode(any())).thenReturn(GameMode.SOLO);
        Mockito.when(persistenceGameServiceMock.getBoats(any(), Mockito.eq(IdPlayer.PLAYER_1))).thenReturn(generateListBoatDTO());

        CoordinateDTO coordinateDTO = new CoordinateDTO(1, 2);

        gameEngineService.playerAttack("ID_GAME", IdPlayer.PLAYER_1, coordinateDTO);

        Mockito.verify(persistenceGameServiceMock).revealCell(eq("ID_GAME"), eq(IdPlayer.PLAYER_2), argumentCaptorCoordinateDTO.capture());
        assertEquals(coordinateDTO, argumentCaptorCoordinateDTO.getValue());

        Mockito.verify(iaPlayerServiceMock).iaAttack(any(), argumentCaptorListBoatType.capture());

        assertEquals(1, argumentCaptorListBoatType.getValue().size());
        assertEquals(BoatType.PORTE_AVIONS.getName(), argumentCaptorListBoatType.getValue().get(0).getName());
    }

    @Test
    void playerJoinGameTest() {
        Mockito.when(persistenceGameServiceMock.isGameExist(any())).thenReturn(true);
        boolean result = gameEngineService.playerJoinGame("TORPILLE_9595");
        Mockito.verify(playerCommunicationServiceMock).playerJoinTheGameEvent(eq("TORPILLE_9595"), eq(IdPlayer.PLAYER_2));
        assertTrue(result);
    }

    @Test
    void playerJoinGameWrongIdTest() {
        Mockito.when(persistenceGameServiceMock.isGameExist(any())).thenReturn(false);
        boolean result = gameEngineService.playerJoinGame("TORPILLE_9595");
        assertFalse(result);
    }

    @Test
    void playerAttackMultiTest() {
        Mockito.doNothing().when(persistenceGameServiceMock).revealCell(any(), any(), any());
        Mockito.doNothing().when(persistenceGameServiceMock).updateStateBoats(any(), any());
        Mockito.doNothing().when(persistenceGameServiceMock).revealCellsNextToDestroyedBoat(any(), any());
        Mockito.when(persistenceGameServiceMock.getGameMode(any())).thenReturn(GameMode.MULTI);
        Mockito.when(persistenceGameServiceMock.getBoats(any(), Mockito.eq(IdPlayer.PLAYER_1))).thenReturn(generateListBoatDTO());

        CoordinateDTO coordinateDTO = new CoordinateDTO(1, 2);

        gameEngineService.playerAttack("ID_GAME", IdPlayer.PLAYER_1, coordinateDTO);

        Mockito.verify(persistenceGameServiceMock).revealCell(eq("ID_GAME"), eq(IdPlayer.PLAYER_2), argumentCaptorCoordinateDTO.capture());
        assertEquals(coordinateDTO, argumentCaptorCoordinateDTO.getValue());

        Mockito.verify(iaPlayerServiceMock, never()).iaAttack(any(), any());
    }


    @Test
    void playerAttackEndGameTest() {

        Mockito.doNothing().when(persistenceGameServiceMock).revealCell(any(), any(), any());
        Mockito.doNothing().when(persistenceGameServiceMock).updateStateBoats(any(), any());
        Mockito.doNothing().when(persistenceGameServiceMock).revealCellsNextToDestroyedBoat(any(), any());
        Mockito.when(persistenceGameServiceMock.getGameMode(any())).thenReturn(GameMode.SOLO);
        Mockito.when(persistenceGameServiceMock.isAllBoatDestroyed(any(), eq(IdPlayer.PLAYER_1))).thenReturn(true);


        CoordinateDTO coordinateDTO = new CoordinateDTO(1, 2);

        gameEngineService.playerAttack("ID_GAME", IdPlayer.PLAYER_1, coordinateDTO);


        Mockito.verify(playerCommunicationServiceMock).endGameEvent(eq("ID_GAME"), argumentCaptorEndGameResultDTO.capture());
        assertEquals(IdPlayer.PLAYER_1, argumentCaptorEndGameResultDTO.getValue().getIdPlayerLose());
        assertEquals(IdPlayer.PLAYER_2, argumentCaptorEndGameResultDTO.getValue().getIdPlayerWin());
    }


    @Test
    void getIdOpponentTest() {
        assertEquals(IdPlayer.PLAYER_2, gameEngineService.getIdOpponent(IdPlayer.PLAYER_1));
        assertEquals(IdPlayer.PLAYER_1, gameEngineService.getIdOpponent(IdPlayer.PLAYER_2));
    }
}
