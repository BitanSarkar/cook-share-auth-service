package com.bitsar.cookshareauthservice.exception;

public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException(String invalidSession) {
        super(invalidSession);
    }
}
