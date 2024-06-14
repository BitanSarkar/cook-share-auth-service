package com.bitsar.passwordlessauth.exception;

public class UserAlreadyLoggedOutException extends RuntimeException {
    public UserAlreadyLoggedOutException(String s) {
        super(s);
    }
}
