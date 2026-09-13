package com.sawiya.authservice.dto;

import lombok.*;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
}
