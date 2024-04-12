package com.patinaud.batailleengine.gameengine;

import com.patinaud.bataillecommunication.communication.PlayerCommunicationService;
import com.patinaud.batailleengine.config.TestConfiguration;
import com.patinaud.bataillemodel.constants.BoatType;
import com.patinaud.bataillemodel.constants.GameMode;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.*;
import com.patinaud.bataillepersistence.persistence.PersistenceService;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
@TestPropertySource(properties = {"ID_GAME_WORDS=AMIRAL, AMPHIBIE, ARSENAL"})
public class GameEngineServiceImplTest {

    @MockBean
    PersistenceService persistenceServiceMock;
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
        Mockito.doNothing().when(persistenceServiceMock).saveGame(any());
        Mockito.doNothing().when(persistenceServiceMock).savePlayer(any());
        Mockito.doNothing().when(persistenceServiceMock).saveGrid(any(), any(), any());
        Mockito.doNothing().when(persistenceServiceMock).setBoatPosition(any(), any(), any());


        Mockito.when(gridServiceMock.generateEmptyGrid(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new GridDTO());

        GameDTO gameDTO = gameEngineService.generateNewGame(GameMode.SOLO);

        assertFalse(gameDTO.getId().isEmpty());

        Mockito.verify(persistenceServiceMock).saveGame(argumentCaptorGameDTO.capture());
        assertEquals(gameDTO.getId(), argumentCaptorGameDTO.getValue().getId());

        Mockito.verify(persistenceServiceMock, Mockito.times(2)).savePlayer(argumentCaptorPlayerDTO.capture());
        assertEquals(IdPlayer.PLAYER_1, argumentCaptorPlayerDTO.getAllValues().get(0).getIdPlayer());
        assertEquals(IdPlayer.PLAYER_2, argumentCaptorPlayerDTO.getAllValues().get(1).getIdPlayer());

    }


    @Test
    void positionHumanPlayerBoatTest() {
        Mockito.doNothing().when(persistenceServiceMock).setBoatPosition(any(), any(), any());

        List<BoatDTO> boats = new ArrayList<>();

        gameEngineService.positionHumanPlayerBoat("ABC", IdPlayer.PLAYER_1, boats);

        Mockito.verify(persistenceServiceMock).setBoatPosition("ABC", IdPlayer.PLAYER_1, boats);

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
    void playerAttackTest() {
        Mockito.doNothing().when(persistenceServiceMock).revealCell(any(), any(), any());
        Mockito.doNothing().when(persistenceServiceMock).updateStateBoats(any(), any());
        Mockito.doNothing().when(persistenceServiceMock).revealCellsNextToDestroyedBoat(any(), any());
        Mockito.when(persistenceServiceMock.getBoats(any(), Mockito.eq(IdPlayer.PLAYER_1))).thenReturn(generateListBoatDTO());

        CoordinateDTO coordinateDTO = new CoordinateDTO(1, 2);

        gameEngineService.playerAttack("ID_GAME", IdPlayer.PLAYER_1.getValue(), coordinateDTO);

        Mockito.verify(persistenceServiceMock).revealCell(eq("ID_GAME"), eq(IdPlayer.PLAYER_2), argumentCaptorCoordinateDTO.capture());
        assertEquals(coordinateDTO, argumentCaptorCoordinateDTO.getValue());

        Mockito.verify(iaPlayerServiceMock).iaAttack(any(), argumentCaptorListBoatType.capture());

        assertEquals(1, argumentCaptorListBoatType.getValue().size());
        assertEquals(BoatType.PORTE_AVIONS.getName(), argumentCaptorListBoatType.getValue().get(0).getName());
    }

    @Test
    void playerAttackWithWrongIdPlayerTest() {

        CoordinateDTO coordinateDTO = new CoordinateDTO(1, 2);


        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> gameEngineService.playerAttack("ID_GAME", "Wrong id player", coordinateDTO)
        );

        assertEquals("No enum constant com.patinaud.bataillemodel.constants.IdPlayer.WRONG ID PLAYER", thrown.getMessage());

    }


    @Test
    void playerAttackEndGameTest() {


        Mockito.doNothing().when(persistenceServiceMock).revealCell(any(), any(), any());
        Mockito.doNothing().when(persistenceServiceMock).updateStateBoats(any(), any());
        Mockito.doNothing().when(persistenceServiceMock).revealCellsNextToDestroyedBoat(any(), any());
        Mockito.when(persistenceServiceMock.isAllBoatDestroyed(any(), eq(IdPlayer.PLAYER_1))).thenReturn(true);


        CoordinateDTO coordinateDTO = new CoordinateDTO(1, 2);

        gameEngineService.playerAttack("ID_GAME", IdPlayer.PLAYER_1.getValue(), coordinateDTO);


        Mockito.verify(playerCommunicationServiceMock).diffuseEndGame(eq("ID_GAME"), argumentCaptorEndGameResultDTO.capture());
        assertEquals(IdPlayer.PLAYER_1, argumentCaptorEndGameResultDTO.getValue().getIdPlayerLose());
        assertEquals(IdPlayer.PLAYER_2, argumentCaptorEndGameResultDTO.getValue().getIdPlayerWin());
    }


    @Test
    void getIdOpponentTest() {
        assertEquals(IdPlayer.PLAYER_2, gameEngineService.getIdOpponent(IdPlayer.PLAYER_1));
        assertEquals(IdPlayer.PLAYER_1, gameEngineService.getIdOpponent(IdPlayer.PLAYER_2));
    }
}
