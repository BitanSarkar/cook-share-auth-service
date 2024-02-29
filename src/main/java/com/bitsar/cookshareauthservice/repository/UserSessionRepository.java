package com.bitsar.cookshareauthservice.repository;

import com.bitsar.cookshareauthservice.entity.UserSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends MongoRepository<UserSession, String> {

    boolean existsByPhoneNumber(String phoneNumber);

    void deleteAllByPhoneNumber(String phoneNumber);

    UserSession findByPhoneNumber(String phoneNumber);
}
