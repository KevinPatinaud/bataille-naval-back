package com.patinaud.bataillepersistence.persistence;

import com.patinaud.bataillemodel.dto.UserDTO;

public interface PersistenceUserService {

    UserDTO registerUser(UserDTO user);
    
    boolean userExistByEmail(String email);
}
