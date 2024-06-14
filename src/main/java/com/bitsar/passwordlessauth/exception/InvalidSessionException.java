package com.bitsar.passwordlessauth.exception;

public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException(String invalidSession) {
        super(invalidSession);
    }
}
