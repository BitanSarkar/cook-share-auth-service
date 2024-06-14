package com.bitsar.passwordlessauth.exception;

public class InvalidRegistrationSessionException extends RuntimeException {
    public InvalidRegistrationSessionException(String invalidRegistrationSession) {
        super(invalidRegistrationSession);
    }
}
