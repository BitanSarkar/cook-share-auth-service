package com.bitsar.cookshareauthservice.exception;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException(String phoneNumberAlreadyExists) {
        super(phoneNumberAlreadyExists);
    }
}
