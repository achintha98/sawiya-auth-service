package com.sawiya.authservice.mapper;

import com.sawiya.authservice.dto.RegisterRequestDTO;
import com.sawiya.authservice.dto.RegisterResponseDTO;
import com.sawiya.authservice.model.UserEntity;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */
public class UserMapper {

    public static RegisterResponseDTO mapToUserResponseDTO(UserEntity userEntity) {
        return RegisterResponseDTO.builder().
                firstName(userEntity.getFirstName()).
                lastName(userEntity.getLastName()).
                email(userEntity.getEmail()).
                build();
    }

    public static UserEntity mapFromUserRequestDTO(RegisterRequestDTO registerRequestDTO) {
        return UserEntity.builder().
                firstName(registerRequestDTO.getFirstName()).lastName(registerRequestDTO.getLastName())
                .email(registerRequestDTO.getEmail()).
                password(registerRequestDTO.getPassword()).build();
    }
}
