package com.patinaud.batailleapi.security;

import com.patinaud.bataillemodel.dto.UserDTO;

public interface AuthService {
    UserDTO registerUser(UserDTO userDto);
}
