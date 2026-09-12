package com.sawiya.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Achintha Kalunayaka
 * @since 9/11/2026
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String message;
    private String status;
    private String token;
}
