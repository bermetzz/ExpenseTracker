package com.example.expensetracker.bot;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionService {
    private final Map<Long, String> userTokens = new ConcurrentHashMap<>();

    public void saveToken(Long userId, String token) {
        userTokens.put(userId, token);
    }

    public String getToken(Long userId) {
        return userTokens.get(userId);
    }

    public boolean hasToken(Long userId) {
        return userTokens.containsKey(userId);
    }
}

