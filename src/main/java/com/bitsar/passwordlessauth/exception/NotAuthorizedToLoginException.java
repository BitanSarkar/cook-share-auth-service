package com.bitsar.passwordlessauth.exception;

public class NotAuthorizedToLoginException extends RuntimeException {
    public NotAuthorizedToLoginException(String s) {
        super(s);
    }
}
