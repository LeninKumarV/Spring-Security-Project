package com.example.security.securityProject.securityModels;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class loginRequest {

    private String username;

    private String password;

    private Set<String> roles;
}

