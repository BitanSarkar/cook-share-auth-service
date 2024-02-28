package com.bitsar.cookshareauthservice.util;

import com.bitsar.cookshareauthservice.dto.ResponseWrap;
import com.bitsar.cookshareauthservice.dto.Status;
import com.bitsar.cookshareauthservice.enums.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {
    private ResponseBuilder() {
    }

    public static <T> ResponseEntity<ResponseWrap<T>> getSuccessfulResponse(T value) {
        return getGenericResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS, "", value);
    }

    public static <T> ResponseEntity<ResponseWrap<T>> getFailureResponse(String description) {
        return getGenericResponse(ResponseStatus.FAILURE.getCode(), ResponseStatus.FAILURE, description, null);
    }

    public static <T> ResponseEntity<ResponseWrap<T>> getFailureResponse(String description, int level) {
        return getGenericResponse(ResponseStatus.FAILURE.getCode() - level, ResponseStatus.FAILURE, description, null);
    }

    private static <T> ResponseEntity<ResponseWrap<T>> getGenericResponse(int code, ResponseStatus status, String description, T value) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrap.<T>builder().status(Status.builder().code(code).description(description).status(status).build()).data(value).build());
    }
}
