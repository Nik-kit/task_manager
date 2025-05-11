package com.tereshchenko.taskmanager.controller;

import com.tereshchenko.taskmanager.service.JwtService;
import com.tereshchenko.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization Controller", description = "Handles user authentication and JWT token generation")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        try {
            String username = request.get("username");
            String password = request.get("password");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            UserDetails user = userService.loadUserByUsername(username);

            String token = jwtService.generateToken(user);

            return Map.of("token", token);

        } catch (BadCredentialsException e) {

        throw new RuntimeException("Invalid credentials", e);
        }
    }
}
