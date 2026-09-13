package com.sawiya.authservice.service;

import com.sawiya.authservice.dto.RegisterRequestDTO;
import com.sawiya.authservice.dto.RegisterResponseDTO;
import com.sawiya.authservice.exception.EmailAlreadyExistsException;
import com.sawiya.authservice.exception.UserNotFoundException;
import com.sawiya.authservice.mapper.UserMapper;
import com.sawiya.authservice.model.UserEntity;
import com.sawiya.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UserNotFoundException {

        UserEntity userEntity = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"));
        return User.withUsername(userEntity.getEmail())
                .password(userEntity.getPassword())
                .build();
    }

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email is already registered");
        }
        registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        UserEntity userEntity = userRepository.save(UserMapper.mapFromUserRequestDTO(registerRequestDTO));
        return UserMapper.mapToUserResponseDTO(userEntity);
    }
}
