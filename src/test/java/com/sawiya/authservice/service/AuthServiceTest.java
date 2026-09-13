package com.sawiya.authservice.service;

import com.sawiya.authservice.controller.AuthController;
import com.sawiya.authservice.dto.LoginRequestDTO;
import com.sawiya.authservice.dto.LoginResponseDTO;
import com.sawiya.authservice.dto.RegisterRequestDTO;
import com.sawiya.authservice.dto.RegisterResponseDTO;
import com.sawiya.authservice.exception.EmailAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Achintha Kalunayaka
 * @since 9/13/2026
 */

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;


    @Test
    void login_shouldReturn200AndLoginResponse_whenAuthenticationSucceeds() {

        // Arrange
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("Password123!");

        LoginResponseDTO expectedResponse = new LoginResponseDTO(
                true,
                "Login successful",
                "jwt-token"
        );

        when(authService.authenticate(request))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<LoginResponseDTO> response =
                authController.login(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(authService).authenticate(request);
    }


    @Test
    void register_shouldReturn201AndRegisterResponse_whenRegistrationSucceeds() {

        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setFirstName("Bruce");
        request.setLastName("Wayne");
        request.setEmail("test@example.com");
        request.setPassword("Password123!");

        RegisterResponseDTO expectedResponse =
                new RegisterResponseDTO(
                        "Bruce",
                        "Wayne",
                        "test@example.com"
                );

        when(userService.register(request))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<RegisterResponseDTO> response =
                authController.register(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(userService).register(request);
    }


    @Test
    void login_shouldPropagateException_whenAuthenticationFails() {

        // Arrange
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        when(authService.authenticate(request))
                .thenThrow(new BadCredentialsException(
                        "Invalid email or password"
                ));

        // Act & Assert
        assertThrows(
                BadCredentialsException.class,
                () -> authController.login(request)
        );

        verify(authService).authenticate(request);
    }


    @Test
    void register_shouldPropagateException_whenEmailAlreadyExists() {

        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("existing@example.com");
        request.setPassword("Password123!");

        when(userService.register(request))
                .thenThrow(new EmailAlreadyExistsException(
                        "Email is already registered"
                ));

        // Act & Assert
        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authController.register(request)
        );

        verify(userService).register(request);
    }
}