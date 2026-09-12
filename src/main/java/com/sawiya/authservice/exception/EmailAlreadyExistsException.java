package com.sawiya.authservice.exception;

/**
 * @author Achintha Kalunayaka
 * @since 4/9/2025
 */
public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
