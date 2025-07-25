package com.diogomekie.binderbuddy.backend.service;

import com.diogomekie.binderbuddy.backend.dto.AuthResponse;
import com.diogomekie.binderbuddy.backend.dto.LoginRequest;
import com.diogomekie.binderbuddy.backend.dto.RegisterRequest;
import com.diogomekie.binderbuddy.backend.model.User;
import com.diogomekie.binderbuddy.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class); // NEW: Logger instance

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return new AuthResponse("User registered successfully!", null, savedUser.getUsername(), savedUser.getId());
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.debug("Authentication object in SecurityContextHolder after setting: {}", SecurityContextHolder.getContext().getAuthentication());

        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        logger.debug("Principal extracted from authentication: {}", principal);

        String token = jwtUtil.generateToken(principal);

        logger.debug("Generated JWT token: {}", token != null ? token.substring(0, 30) + "..." : "null"); // Log truncated token

        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        return new AuthResponse("User logged in successfully!", token, user.getUsername(), user.getId());
    }
}
