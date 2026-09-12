package com.sawiya.authservice.mapper;

import com.sawiya.authservice.dto.UserResponseDTO;
import com.sawiya.authservice.model.User;

import java.time.LocalDate;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */
public class UserMapper {

    public static UserResponseDTO mapToUserResponseDTO(User user) {
        return UserResponseDTO.builder().
                firstName(user.getFirstName()).
                lastName(user.getLastName()).
                email(user.getEmail()).
                build();
    }


}
