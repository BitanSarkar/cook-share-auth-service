package com.bitsar.passwordlessauth.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document(collection = "users_session")
@Data
@Builder
public class UserSession {
    @MongoId
    private String id;
    @Indexed(unique = true)
    private String session;
    @Indexed(unique = true)
    private String phoneNumber;
    private String accessToken;
    private String idToken;
    private String refreshToken;
    private LocalDateTime timeStamp;
}
