package com.bitsar.passwordlessauth.dto;

import com.bitsar.passwordlessauth.enums.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Status {
    private long code;
    private ResponseStatus status;
    private String description;
}
