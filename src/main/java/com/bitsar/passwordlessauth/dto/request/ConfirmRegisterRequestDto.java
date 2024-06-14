package com.bitsar.passwordlessauth.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmRegisterRequestDto {
    private String sessionId;
    private String confirmationCode;
}
