package com.sawiya.authservice.controller;

import com.sawiya.authservice.dto.LoginRequestDTO;
import com.sawiya.authservice.dto.LoginResponseDTO;
import com.sawiya.authservice.dto.RegisterRequestDTO;
import com.sawiya.authservice.dto.RegisterResponseDTO;
import com.sawiya.authservice.exception.EmailAlreadyExistsException;
import com.sawiya.authservice.service.AuthService;
import com.sawiya.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for user authentication and registration.
 *
 * <p>Provides endpoints for registering new users and authenticating
 * existing users using their email and password.</p>
 *
 * @author Achintha Kalunayaka
 * @since 9/11/2026
 */

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    /**
     * Authenticates a user using their email and password and returns a JWT
     * upon successful authentication.
     *
     * @param request login credentials containing the user's email and password
     * @return a login response containing the authentication result and JWT
     * @throws AuthenticationException if the provided credentials are invalid
     */
    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO loginResponseDTO = authService.authenticate(request);
        return ResponseEntity.ok(loginResponseDTO);
    }

    /**
     * Registers a new user account after validating the provided registration
     * details.
     *
     * @param request registration details including the user's name, email,
     *                and password
     * @return a registration response with HTTP 201 Created when registration
     *         is successful
     * @throws EmailAlreadyExistsException if the email is already registered
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        RegisterResponseDTO registerResponseDTO = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponseDTO);
    }
}
