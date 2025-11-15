package com.example.security.securityProject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "authorities",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userid", "authority"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "authid", columnDefinition = "UUID")
    private UUID authId;

    @Column(length = 50, nullable = false)
    private String authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "userid",
            referencedColumnName = "userid",
            nullable = false
    )
    private Users user;
}
