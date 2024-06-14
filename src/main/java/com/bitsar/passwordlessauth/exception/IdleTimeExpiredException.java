package com.bitsar.passwordlessauth.exception;

public class IdleTimeExpiredException extends RuntimeException {
    public IdleTimeExpiredException(String s) {
        super(s);
    }
}
