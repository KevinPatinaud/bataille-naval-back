package com.patinaud.bataillepersistence.dao;

import com.patinaud.bataillepersistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    User save(User toEntity);
}
