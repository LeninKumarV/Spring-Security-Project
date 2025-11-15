package com.example.security.securityProject.controllers;

import com.example.security.securityProject.entity.Authority;
import com.example.security.securityProject.entity.Users;
import com.example.security.securityProject.jwt.AuthEntryPointJwt;
import com.example.security.securityProject.jwt.JwtAuthTokenFilter;
import com.example.security.securityProject.jwt.jwtUtils;
import com.example.security.securityProject.repository.UsersRepository;
import com.example.security.securityProject.securityModels.loginRequest;
import com.example.security.securityProject.securityModels.loginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class securityControllers {

    private final jwtUtils jwtUtils;
    private final JwtAuthTokenFilter jwtAuthTokenFilter;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "hello")
    public String getGreeting(){
        return "Hello World";
    }

    @PreAuthorize(value = "hasRole('USER')")
    @GetMapping("user")
    public String getUserDetails(){
        return "Hello User!";
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("admin")
    public String getAdminDetails(){
        return "Hello Admin!";
    }


    @PostMapping(value =  "/login")
    public ResponseEntity<?> authenticateUser(@RequestBody loginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Invalid credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        log.info("Authentication Success {}", authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        loginResponse loginRes = loginResponse.builder().jwtToken(jwtToken).username(userDetails.getUsername()).
        roles(roles).build();

        return new ResponseEntity<>(loginRes, HttpStatus.OK);
    }

    // New endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody loginRequest request) {

        if (usersRepository.existsByUsername((request.getUsername()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Username already exists"));
        }

        Users newUser = Users.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .build();

        Authority roleUser = Authority.builder()
                .authority("ROLE_USER")
                .build();

        newUser.addAuthority(roleUser);

        usersRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }

}
