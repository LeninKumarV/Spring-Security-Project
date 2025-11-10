package com.example.security.securityProject.securityModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class loginResponse {

    private String jwtToken;

    private String username;

    private List<String> roles;
}
