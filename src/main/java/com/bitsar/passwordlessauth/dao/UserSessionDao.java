package com.bitsar.passwordlessauth.dao;

import com.bitsar.passwordlessauth.entity.UserSession;
import com.bitsar.passwordlessauth.repository.UserSessionRepository;
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

    public UserSession findByPhoneNumber(String phoneNumber) {
        return userSessionRepository.findByPhoneNumber(phoneNumber);
    }

    public void deleteByPhoneNumber(String phoneNumber) {
        userSessionRepository.deleteAllByPhoneNumber(phoneNumber);
    }
}
