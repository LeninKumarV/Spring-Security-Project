package com.example.security.securityProject.repository;
import com.example.security.securityProject.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
}

