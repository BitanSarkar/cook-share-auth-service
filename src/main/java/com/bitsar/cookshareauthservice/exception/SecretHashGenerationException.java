package com.bitsar.cookshareauthservice.exception;

public class SecretHashGenerationException extends RuntimeException {
    public SecretHashGenerationException(String errorWhileCalculatingSecretHash, Exception e) {
        super(errorWhileCalculatingSecretHash, e);
    }
}
