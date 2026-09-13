package com.sawiya.authservice.exception;

/**
 * @author Achintha Kalunayaka
 * @since 9/12/2026
 */
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
