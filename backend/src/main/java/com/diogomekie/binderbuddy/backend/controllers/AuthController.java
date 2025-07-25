package com.diogomekie.binderbuddy.backend.controllers;

import com.diogomekie.binderbuddy.backend.dto.AuthResponse;
import com.diogomekie.binderbuddy.backend.dto.LoginRequest;
import com.diogomekie.binderbuddy.backend.dto.RegisterRequest;
import com.diogomekie.binderbuddy.backend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            return new ResponseEntity<>(new AuthResponse(e.getMessage(), null, null, null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // OMG>>>> WHY?!?!?!?!?!?
            System.err.println("-----------------------------------------------------");
            System.err.println("!!! EXCEPTION CAUGHT IN AUTHCONTROLLER LOGIN !!!");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace(System.err);
            System.err.println("-----------------------------------------------------");

            return new ResponseEntity<>(new AuthResponse("Invalid username or password.", null, null, null), HttpStatus.UNAUTHORIZED);
        }
    }
}