package com.sawiya.authservice.service;

import com.sawiya.authservice.dto.LoginRequestDTO;
import com.sawiya.authservice.dto.LoginResponseDTO;
import com.sawiya.authservice.dto.UserResponseDTO;
import com.sawiya.authservice.model.User;
import com.sawiya.authservice.util.ApiMessage;
import com.sawiya.authservice.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Achintha Kalunayaka
 * @since 9/11/2026
 */

@Service
public class AuthService {

        private final AuthenticationManager authenticationManager;
        private final JwtUtil jwtUtil;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {

            this.authenticationManager = authenticationManager;
            this.jwtUtil = jwtUtil;
    }
        public LoginResponseDTO authenticate(LoginRequestDTO request)  {
                    Authentication authentication =
                            authenticationManager.authenticate(
                                    new UsernamePasswordAuthenticationToken(
                                            request.getEmail(),
                                            request.getPassword()
                                    )
                            );

                    String email = authentication.getName();
                    String token = jwtUtil.generateToken(email);
                return new LoginResponseDTO(
                        "true",
                        ApiMessage.LOGIN_SUCCESS,
                        token
                );
        }
    }
