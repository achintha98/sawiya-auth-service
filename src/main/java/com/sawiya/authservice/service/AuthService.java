package com.sawiya.authservice.service;

import com.sawiya.authservice.dto.LoginRequestDTO;
import com.sawiya.authservice.dto.LoginResponseDTO;
import com.sawiya.authservice.util.ApiMessage;
import com.sawiya.authservice.util.JWTUtilityComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling user authentication and JWT generation.
 *
 * <p>Uses Spring Security's {@link AuthenticationManager} to authenticate
 * user credentials and generates a JWT upon successful authentication.</p>
 *
 * @author Achintha Kalunayaka
 * @since 9/11/2026
 */

@Service
@RequiredArgsConstructor
public class AuthService {

        private final AuthenticationManager authenticationManager;
        private final JWTUtilityComponent JWTUtilityComponentC;

    /**
     * Authenticates a user using their email and password and generates a JWT
     * for successful authentication.
     *
     * @param request login credentials containing the user's email and password
     * @return a login response containing the authentication status, success
     *         message, and generated JWT
     * @throws AuthenticationException if the provided credentials are invalid
     */
    public LoginResponseDTO authenticate(LoginRequestDTO request)  {
                    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                            request.getEmail(),
                                            request.getPassword()));
                    String email = authentication.getName();
                    String token = JWTUtilityComponentC.generateToken(email);
                return new LoginResponseDTO(
                        true,
                        ApiMessage.LOGIN_SUCCESS,
                        token
                );
        }
    }
