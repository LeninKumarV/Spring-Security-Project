package com.example.security.securityProject.repository;

import com.example.security.securityProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, String> {
    boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);
}
