package com.bitsar.cookshareauthservice.exception;

public class InvalidRegistrationSessionException extends RuntimeException {
    public InvalidRegistrationSessionException(String invalidRegistrationSession) {
        super(invalidRegistrationSession);
    }
}
