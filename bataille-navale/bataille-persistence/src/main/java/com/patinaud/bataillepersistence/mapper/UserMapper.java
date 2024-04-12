package com.patinaud.bataillepersistence.mapper;

import com.patinaud.bataillemodel.dto.UserDTO;
import com.patinaud.bataillepersistence.entity.User;

public class UserMapper {
    public static User toEntity(UserDTO dto) {
        User entity = new User();
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());

        return entity;
    }


    public static UserDTO toDto(User entity) {
        UserDTO dto = new UserDTO();
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());

        return dto;
    }
}
