package com.bitsar.passwordlessauth.exception;

public class SecretHashGenerationException extends RuntimeException {
    public SecretHashGenerationException(String errorWhileCalculatingSecretHash, Exception e) {
        super(errorWhileCalculatingSecretHash, e);
    }
}
