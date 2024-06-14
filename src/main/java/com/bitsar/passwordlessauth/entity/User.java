package com.bitsar.passwordlessauth.entity;

import com.bitsar.passwordlessauth.enums.UserStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "users")
@Data
@Builder
public class User {
    @MongoId
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String phoneNumber;
    @Indexed(unique = true)
    private String cognitoUserName;
    private UserStatus status;
}
