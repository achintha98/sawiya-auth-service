package com.sawiya.authservice.dto;

import lombok.Data;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */

@Data
public class RegisterResponseDTO {

    private String firstName;

    private String lastName;

    private String email;
}
