package com.sawiya.authservice.service;

import com.sawiya.authservice.dto.RegisterRequestDTO;
import com.sawiya.authservice.dto.UserResponseDTO;
import com.sawiya.authservice.model.User;
import com.sawiya.authservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public UserResponseDTO loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        ));

        return UserResponseDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

}


