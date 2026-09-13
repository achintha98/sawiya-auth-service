package com.sawiya.authservice.controller;

import com.sawiya.authservice.dto.LoginRequestDTO;
import com.sawiya.authservice.dto.LoginResponseDTO;
import com.sawiya.authservice.dto.RegisterRequestDTO;
import com.sawiya.authservice.dto.RegisterResponseDTO;
import com.sawiya.authservice.service.AuthService;
import com.sawiya.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Achintha Kalunayaka
 * @since 9/11/2026
 */

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO loginResponseDTO = authService.authenticate(request);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        RegisterResponseDTO registerResponseDTO = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponseDTO);
    }
}
