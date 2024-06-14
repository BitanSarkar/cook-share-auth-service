package com.bitsar.passwordlessauth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmLoginResponseDto {
    private String status;
    private String accessToken;
    private String firstName;
    private String lastName;
}
