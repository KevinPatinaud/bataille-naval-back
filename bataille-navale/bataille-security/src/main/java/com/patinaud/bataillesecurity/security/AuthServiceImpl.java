package com.patinaud.bataillesecurity.security;

import com.patinaud.bataillemodel.dto.UserDTO;
import com.patinaud.bataillepersistence.persistence.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final PersistenceService persistenceService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public UserDTO registerUser(UserDTO userDto) {
        if (persistenceService.userExistByEmail(userDto.getEmail())) {
            throw new RuntimeException("Erreur: L'adresse email est déjà utilisée.");
        }

        UserDTO user = new UserDTO();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return persistenceService.saveUser(user);
    }
}
