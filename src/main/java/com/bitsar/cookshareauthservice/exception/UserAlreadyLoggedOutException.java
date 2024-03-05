package com.bitsar.cookshareauthservice.exception;

public class UserAlreadyLoggedOutException extends RuntimeException {
    public UserAlreadyLoggedOutException(String s) {
        super(s);
    }
}
