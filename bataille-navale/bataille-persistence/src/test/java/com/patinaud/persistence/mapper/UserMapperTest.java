package com.patinaud.persistence.mapper;

import com.patinaud.bataillemodel.dto.UserDTO;
import com.patinaud.bataillepersistence.entity.User;
import com.patinaud.bataillepersistence.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserMapperTest {

    @Test
    void utilityClassConstructorShouldThrowException() throws NoSuchMethodException {
        Constructor<UserMapper> constructor = UserMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        Assertions.assertTrue(exception.getCause() instanceof IllegalStateException);
        assertEquals("Utility class", exception.getCause().getMessage());
    }

    @Test
    public void testToEntity() {
        UserDTO dto = new UserDTO();
        dto.setUsername("testUser");
        dto.setEmail("test@example.com");
        dto.setPassword("password123");


        User entity = UserMapper.toEntity(dto);

        assertEquals(dto.getUsername(), entity.getUsername(), "Les noms d'utilisateur ne correspondent pas.");
        assertEquals(dto.getEmail(), entity.getEmail(), "Les emails ne correspondent pas.");
        assertEquals(dto.getPassword(), entity.getPassword(), "Les mots de passe ne correspondent pas.");
    }

    @Test
    public void testToDto() {
        User entity = new User();
        entity.setUsername("testUser");
        entity.setEmail("test@example.com");
        entity.setPassword("password123");


        UserDTO dto = UserMapper.toDto(entity);

        assertEquals(entity.getUsername(), dto.getUsername(), "Les noms d'utilisateur ne correspondent pas.");
        assertEquals(entity.getEmail(), dto.getEmail(), "Les emails ne correspondent pas.");
        assertEquals(entity.getPassword(), dto.getPassword(), "Les mots de passe ne correspondent pas.");
    }
}
