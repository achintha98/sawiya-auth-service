package com.sawiya.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sawiya.authservice.dto.LoginRequestDTO;
import com.sawiya.authservice.dto.LoginResponseDTO;
import com.sawiya.authservice.dto.RegisterRequestDTO;
import com.sawiya.authservice.dto.RegisterResponseDTO;
import com.sawiya.authservice.exception.EmailAlreadyExistsException;
import com.sawiya.authservice.service.AuthService;
import com.sawiya.authservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Achintha Kalunayaka
 * @since 9/13/2026
 */

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @Test
    void signin_shouldReturn200_whenCredentialsAreValid() throws Exception {

        LoginResponseDTO response = new LoginResponseDTO(
                true,
                "Login successful",
                "jwt-token"
        );

        when(authService.authenticate(any(LoginRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "email": "test@example.com",
                            "password": "Password123!"
                        }
                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(true))
                .andExpect(jsonPath("$.status").value("Login successful"))
                .andExpect(jsonPath("$.token").value("jwt-token"));

        verify(authService).authenticate(any(LoginRequestDTO.class));
    }

    @Test
    void signin_shouldReturn400_whenEmailIsInvalid() throws Exception {

        mockMvc.perform(
                        post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "email": "not-an-email",
                            "password": "Password123!"
                        }
                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Please provide a valid email address"));
        verifyNoInteractions(authService);
    }

    @Test
    void signin_shouldReturn400_whenEmailIsMissing() throws Exception {

        mockMvc.perform(
                        post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "password": "Password123!"
                        }
                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                .value("Email is required"));

        verifyNoInteractions(authService);
    }

    @Test
    void signin_shouldReturn400_whenPasswordIsMissing() throws Exception {

        mockMvc.perform(
                        post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "email": "test@example.com"
                        }
                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Password is required"));

        verifyNoInteractions(authService);
    }

    @Test
    void signin_shouldReturn401_whenAuthenticationFails() throws Exception {

        when(authService.authenticate(any(LoginRequestDTO.class)))
                .thenThrow(new BadCredentialsException(
                        "Invalid email or password"
                ));

        mockMvc.perform(
                        post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "email": "test@example.com",
                            "password": "wrongPassword"
                        }
                    """)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(false))
                .andExpect(jsonPath("$.status").value("Invalid email or password"));
    }

    @Test
    void register_shouldReturn201_whenRegistrationIsSuccessful() throws Exception {

        RegisterResponseDTO response = new RegisterResponseDTO(
                "Bruce",
                "Wayne",
                "new@example.com"

        );

        when(userService.register(any(RegisterRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "firstName":"Bruce",
                            "lastName":"Wayne",
                            "email": "new@example.com",
                            "password": "Password123!"
                        }
                    """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Bruce"))
                .andExpect(jsonPath("$.lastName").value("Wayne"))
                .andExpect(jsonPath("$.email").value("new@example.com"));

        verify(userService).register(any(RegisterRequestDTO.class));
    }

    @Test
    void register_shouldReturn409_whenEmailAlreadyExists() throws Exception {

        when(userService.register(any(RegisterRequestDTO.class)))
                .thenThrow(new EmailAlreadyExistsException(
                        "Email is already registered"
                ));

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "firstName":"Bruce",
                            "lastName":"Wayne",
                            "email": "existing@example.com",
                            "password": "Password123!"
                        }
                    """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void register_shouldReturn400_whenEmailIsInvalid() throws Exception {

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "firstName":"Bruce",
                            "lastName":"Wayne",
                            "email": "invalid-email",
                            "password": "Password123!"
                        }
                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Please provide a valid email address"));

        verifyNoInteractions(userService);
    }

    @Test
    void register_shouldReturn400_whenPasswordHasNoSpecialCharacter() throws Exception {

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                    {
                        "firstName": "Bruce",
                        "lastName": "Wayne",
                        "email": "bruce@example.com",
                        "password": "Password123"
                    }
                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Password must contain at least one special character"));
    }
}
