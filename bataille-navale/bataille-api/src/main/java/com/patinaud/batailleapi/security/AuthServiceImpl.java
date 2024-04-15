package com.patinaud.batailleapi.security;

import com.patinaud.bataillemodel.dto.UserDTO;
import com.patinaud.bataillepersistence.persistence.PersistenceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final PersistenceUserService persistenceUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(PersistenceUserService persistenceUserService) {
        this.persistenceUserService = persistenceUserService;
    }

    @Override
    public UserDTO registerUser(UserDTO userDto) {
        if (persistenceUserService.userExistByEmail(userDto.getEmail())) {
            throw new RuntimeException("Erreur: L'adresse email est déjà utilisée.");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return persistenceUserService.registerUser(userDto);
    }
}