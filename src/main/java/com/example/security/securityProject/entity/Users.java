package com.example.security.securityProject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userid", columnDefinition = "UUID")
    private UUID userId;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 500, nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    public void addAuthority(Authority authority) {
        authority.setUser(this);
        authorities.add(authority);
    }
}

