package com.sawiya.authservice.exception;

import com.sawiya.authservice.dto.ErrorResponseDTO;
import jakarta.validation.Valid;
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
 * Provides centralized exception handling for REST API requests.
 *
 * <p>Handles application and validation exceptions and maps them to
 * appropriate HTTP status codes and error response bodies.</p>
 *
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles authentication failures caused by invalid user credentials.
     *
     * @param exception the authentication exception thrown by Spring Security
     * @return an error response with HTTP 401 Unauthorized
     */
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

    /**
     * Handles cases where a requested user cannot be found.
     *
     * @param exception the exception indicating that the user does not exist
     * @return an error response with HTTP 404 Not Found
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException exception) {
        Map<String, String> errorsMap = new HashMap<>();
        logger.warn("User does not exists {}", exception.getMessage());
        errorsMap.put("message", "User does not exists");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorsMap);
    }

    /**
     * Handles validation failures for requests annotated with {@link Valid}.
     *
     * <p>Returns the first validation error message with HTTP 400 Bad Request.</p>
     *
     * @param exception the exception containing validation errors
     * @return an error response with HTTP 400 Bad Request
     */
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

    /**
     * Handles attempts to register a user with an email address that
     * is already registered.
     *
     * @param exception the exception indicating that the email already exists
     * @return an error response with HTTP 409 Conflict
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        Map<String, String> errorsMap = new HashMap<>();
        logger.warn("Email already exists {}", exception.getMessage());
        errorsMap.put("message", "Email already exists");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorsMap);
    }
}

