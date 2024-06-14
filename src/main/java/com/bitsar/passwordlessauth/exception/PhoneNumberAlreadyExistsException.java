package com.bitsar.passwordlessauth.exception;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException(String phoneNumberAlreadyExists) {
        super(phoneNumberAlreadyExists);
    }
}
