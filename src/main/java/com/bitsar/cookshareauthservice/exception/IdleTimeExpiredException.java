package com.bitsar.cookshareauthservice.exception;

public class IdleTimeExpiredException extends RuntimeException {
    public IdleTimeExpiredException(String s) {
        super(s);
    }
}
