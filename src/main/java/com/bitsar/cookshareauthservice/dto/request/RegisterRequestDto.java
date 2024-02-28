package com.bitsar.cookshareauthservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterRequestDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
