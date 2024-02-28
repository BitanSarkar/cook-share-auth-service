package com.bitsar.cookshareauthservice.dao;

import com.bitsar.cookshareauthservice.entity.UserSession;
import com.bitsar.cookshareauthservice.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSessionDao {

    private final UserSessionRepository userSessionRepository;

    public UserSession findBySessionId(String sessionId) {
        return userSessionRepository.findById(sessionId).orElse(null);
    }

    public UserSession findAndUpdateAndSaveSession(UserSession build) {
        if(userSessionRepository.existsByPhoneNumber(build.getPhoneNumber())) userSessionRepository.deleteAllByPhoneNumber(build.getPhoneNumber());
        return userSessionRepository.save(build);
    }

    public void updateSession(UserSession userSession) {
        userSessionRepository.save(userSession);
    }
}
