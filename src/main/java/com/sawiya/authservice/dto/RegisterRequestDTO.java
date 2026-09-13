package com.sawiya.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "FirstName is required")
    private String firstName;

    @NotBlank(message = "LastName is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = ".*[^a-zA-Z0-9].*",
            message = "Password must contain at least one special character"
    )
    private String password;

}
