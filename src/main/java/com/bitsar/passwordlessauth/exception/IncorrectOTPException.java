package com.bitsar.passwordlessauth.exception;

public class IncorrectOTPException extends RuntimeException {
    public IncorrectOTPException(String wrongOtpPassed) {
        super(wrongOtpPassed);
    }
}
