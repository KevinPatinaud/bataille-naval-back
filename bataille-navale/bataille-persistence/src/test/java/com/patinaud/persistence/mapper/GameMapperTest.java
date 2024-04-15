package com.patinaud.persistence.mapper;


import com.patinaud.bataillemodel.constants.GameMode;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.GameDTO;
import com.patinaud.bataillepersistence.entity.Game;
import com.patinaud.bataillepersistence.mapper.GameMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameMapperTest {
    @Test
    void utilityClassConstructorShouldThrowException() throws NoSuchMethodException {
        Constructor<GameMapper> constructor = GameMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        Assertions.assertTrue(exception.getCause() instanceof IllegalStateException);
        assertEquals("Utility class", exception.getCause().getMessage());
    }

    @Test
    public void testToEntity() {

        GameDTO dto = new GameDTO();
        dto.setId("idGame");
        dto.setIdPlayerTurn(IdPlayer.PLAYER_1);
        dto.setMode(GameMode.MULTI);

        Game entity = GameMapper.toEntity(dto);

        assertEquals(dto.getId(), entity.getId(), "Les ID ne correspondent pas.");
        assertEquals(dto.getIdPlayerTurn(), entity.getIdPlayerTurn(), "Les ID des joueurs ne correspondent pas.");
        assertEquals(dto.getMode(), entity.getMode(), "Les modes de jeu ne correspondent pas.");
    }

    @Test
    public void testToDto() {
        Game entity = new Game();
        entity.setId("idGame");
        entity.setIdPlayerTurn(IdPlayer.PLAYER_1);
        entity.setMode(GameMode.MULTI);

        GameDTO dto = GameMapper.toDto(entity);

        assertEquals(entity.getId(), dto.getId(), "Les ID ne correspondent pas.");
        assertEquals(entity.getIdPlayerTurn(), dto.getIdPlayerTurn(), "Les ID des joueurs ne correspondent pas.");
        assertEquals(entity.getMode(), dto.getMode(), "Les modes de jeu ne correspondent pas.");
    }
}