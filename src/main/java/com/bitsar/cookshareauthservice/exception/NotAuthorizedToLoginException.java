package com.bitsar.cookshareauthservice.exception;

public class NotAuthorizedToLoginException extends RuntimeException {
    public NotAuthorizedToLoginException(String s) {
        super(s);
    }
}
