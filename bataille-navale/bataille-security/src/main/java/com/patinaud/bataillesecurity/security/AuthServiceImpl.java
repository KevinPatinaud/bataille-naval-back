package com.patinaud.bataillesecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthServiceImpl {

/*
    @Autowired
    private UserRepository userRepository;

 */

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
    public User registerUser(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Erreur: L'adresse email est déjà utilisée.");
        }

        // Création d'un nouvel utilisateur
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return userRepository.save(user);
    }

     */
}
