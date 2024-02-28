package com.bitsar.cookshareauthservice.enums;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    SUCCESS(1000),
    FAILURE(1999);

    private final int code;

    ResponseStatus(int code) {
        this.code = code;
    }

}
