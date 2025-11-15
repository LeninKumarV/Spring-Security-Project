package com.example.security.securityProject.security;

import com.example.security.securityProject.UsersService.JpaUserDetailsService;
import com.example.security.securityProject.entity.Authority;
import com.example.security.securityProject.entity.Users;
import com.example.security.securityProject.jwt.AuthEntryPointJwt;
import com.example.security.securityProject.jwt.JwtAuthTokenFilter;
import com.example.security.securityProject.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurity {

    private final AuthEntryPointJwt authEntryPointJwt;
    private final JpaUserDetailsService userDetailsService;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Use JPA-based service
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    // Optional (explicit provider registration)
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    // Initialize default users if they don't exist
    @Bean
    public CommandLineRunner initData(UsersRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findById("user1").isEmpty()) {
                Users user1 = Users.builder()
                        .username("user1")
                        .password(passwordEncoder.encode("password1"))
                        .enabled(true)
                        .build();
                user1.addAuthority(Authority.builder().authority("ROLE_USER").build());
                userRepository.save(user1);
            }

            if (userRepository.findById("admin").isEmpty()) {
                Users admin = Users.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("adminPass"))
                        .enabled(true)
                        .build();
                admin.addAuthority(Authority.builder().authority("ROLE_ADMIN").build());
                userRepository.save(admin);
            }

            log.info("Default users ensured in database!");
        };
    }
}
