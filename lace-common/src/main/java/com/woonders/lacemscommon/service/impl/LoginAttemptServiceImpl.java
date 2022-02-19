package com.woonders.lacemscommon.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.woonders.lacemscommon.config.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Emanuele on 17/09/2016.
 */
@Service
public class LoginAttemptServiceImpl {

    private int maxAttempts;
    private LoadingCache<String, Integer> attemptsCache;

    @PostConstruct
    public void init() {
        maxAttempts = Constants.MAX_LOGIN_ATTEMPTS;
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(final String key) {
                        return 0;
                    }
                });
    }

    public void storeSucceededLogin(final String key) {
        attemptsCache.invalidate(key);
    }

    public void storeFailedLogin(final String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(final String key) {
        try {
            return attemptsCache.get(key) >= maxAttempts;
        } catch (final ExecutionException e) {
            return false;
        }
    }
}
