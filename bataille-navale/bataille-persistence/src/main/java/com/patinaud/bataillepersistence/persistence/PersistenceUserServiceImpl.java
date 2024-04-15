package com.patinaud.bataillepersistence.persistence;

import com.patinaud.bataillemodel.dto.UserDTO;
import com.patinaud.bataillepersistence.dao.UserRepository;
import com.patinaud.bataillepersistence.entity.User;
import com.patinaud.bataillepersistence.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class PersistenceUserServiceImpl implements PersistenceUserService {
    
    UserRepository userRepository;

    public PersistenceUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO registerUser(UserDTO user) {
        User userSaved = userRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDto(userSaved);
    }

    @Override
    public boolean userExistByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
