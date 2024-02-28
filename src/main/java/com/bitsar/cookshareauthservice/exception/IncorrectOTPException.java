package com.bitsar.cookshareauthservice.exception;

public class IncorrectOTPException extends RuntimeException {
    public IncorrectOTPException(String wrongOtpPassed) {
        super(wrongOtpPassed);
    }
}
