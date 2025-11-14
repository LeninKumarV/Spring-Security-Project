package com.example.security.securityProject.repository;

import com.example.security.securityProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {
}
