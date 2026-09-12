package com.sawiya.authservice.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */

@Data
@Builder
public class UserResponseDTO {

    private String firstName;

    private String lastName;

    private String email;

}
