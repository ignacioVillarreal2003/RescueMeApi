package com.api.rescuemeapi.infrastructure.persistence;

import com.api.rescuemeapi.domain.dtos.user.InitiateRegisterUserRequest;
import com.api.rescuemeapi.domain.dtos.user.UserRegisterSagaResponse;
import com.api.rescuemeapi.domain.dtos.user.UserResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Component
public class SagaStore {

    private final Cache<UUID, InitiateRegisterUserRequest> initiateRegisterUserCache;
    private final Cache<UUID, UserRegisterSagaResponse> registerUserSagaCache;

    public SagaStore() {
        this.initiateRegisterUserCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10))
                .maximumSize(10_000)
                .build();
        this.registerUserSagaCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10))
                .maximumSize(10_000)
                .build();
    }

    public void putInitiateRegisterUser(UUID sagaId, InitiateRegisterUserRequest request) {
        initiateRegisterUserCache.put(sagaId, request);
    }

    public Optional<InitiateRegisterUserRequest> getInitiateRegisterUser(UUID sagaId) {
        return Optional.ofNullable(initiateRegisterUserCache.getIfPresent(sagaId));
    }

    public void removeInitiateRegisterUser(UUID sagaId) {
        initiateRegisterUserCache.invalidate(sagaId);
    }

    public void putSuccessRegisterUserSagaCache(UUID sagaId, UserResponse response, String token, String refreshToken) {
        registerUserSagaCache.put(sagaId, UserRegisterSagaResponse.builder()
                .success(true)
                .user(response)
                .errorMessage(null)
                .token(token)
                .refreshToken(refreshToken)
                .build());
    }

    public void putFailureRegisterUserSagaCache(UUID sagaId, String error) {
        registerUserSagaCache.put(sagaId, UserRegisterSagaResponse.builder()
                .success(false)
                .user(null)
                .errorMessage(error)
                .build());
    }

    public Optional<UserRegisterSagaResponse> getRegisterUserSagaCache(UUID sagaId) {
        return Optional.ofNullable(registerUserSagaCache.getIfPresent(sagaId));
    }

    public void removeRegisterUserSagaCache(UUID sagaId) {
        registerUserSagaCache.invalidate(sagaId);
    }
}
