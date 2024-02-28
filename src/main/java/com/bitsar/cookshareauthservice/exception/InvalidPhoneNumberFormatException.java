package com.bitsar.cookshareauthservice.exception;

public class InvalidPhoneNumberFormatException extends RuntimeException {
    public InvalidPhoneNumberFormatException(String s) {
        super(s);
    }
}
