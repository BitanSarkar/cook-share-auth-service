package com.bitsar.cookshareauthservice.repository;

import com.bitsar.cookshareauthservice.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    boolean existsByCognitoUserName(String cognitoUserName);

    User findByCognitoUserName(String cognitoUserName);
}
