package com.sawiya.authservice.exception;

import com.sawiya.authservice.dto.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentialsException(AuthenticationException exception) {
        logger.warn("Invalid email or password {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        false,
                        "Invalid email or password"
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException exception) {
        Map<String, String> errorsMap = new HashMap<>();
        logger.warn("User does not exists {}", exception.getMessage());
        errorsMap.put("message", "User does not exists");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorsMap);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException exception) {
        Map<String, String> errorsMap = new HashMap<>();
        logger.warn("Invalid login request {}", exception.getMessage());
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Invalid request");
        errorsMap.put("message", message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorsMap);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        Map<String, String> errorsMap = new HashMap<>();
        logger.warn("Email already exists {}", exception.getMessage());
        errorsMap.put("message", "Email already exists");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorsMap);
    }
}

