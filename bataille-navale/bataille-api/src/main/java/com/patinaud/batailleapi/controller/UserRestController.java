package com.patinaud.batailleapi.controller;


import com.patinaud.batailleapi.request.SignUpRequest;
import com.patinaud.batailleapi.security.AuthService;
import com.patinaud.bataillemodel.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class UserRestController {

    @Autowired
    AuthService authService;

    public UserRestController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        
        authService.registerUser(new UserDTO(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword()));
        return ResponseEntity.ok("Utilisateur enregistré avec succès!");
    }


}
