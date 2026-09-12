package com.sawiya.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */

@Data
@Builder
public class RegisterRequestDTO {

    @NotBlank(message = "Email is required")
    private String firstName;

    @NotBlank(message = "Email is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

}
