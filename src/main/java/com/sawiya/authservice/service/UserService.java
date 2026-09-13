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
 * Service responsible for user registration and integration with
 * Spring Security's user authentication mechanism.
 *
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Loads a user by their email address for authentication by Spring Security.
     *
     * @param email the email address used as the user's username
     * @return the user's Spring Security {@link UserDetails}
     * @throws UserNotFoundException if no user exists with the provided email
     */
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


    /**
     * Registers a new user after validating that the email address is not
     * already registered and securely encoding the user's password.
     *
     * @param registerRequestDTO registration details containing the user's
     *                           name, email, and password
     * @return a response containing the newly registered user's details
     * @throws EmailAlreadyExistsException if the email address is already
     *                                     registered
     */
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
