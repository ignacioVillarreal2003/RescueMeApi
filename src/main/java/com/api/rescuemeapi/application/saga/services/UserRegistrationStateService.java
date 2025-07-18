package com.api.rescuemeapi.application.saga.services;

import com.api.rescuemeapi.application.exceptions.SagaNotFoundException;
import com.api.rescuemeapi.domain.dtos.user.UserRegisterSagaResponse;
import com.api.rescuemeapi.domain.dtos.user.UserResponse;
import com.api.rescuemeapi.domain.saga.state.UserRegistrationState;
import com.api.rescuemeapi.domain.saga.step.UserRegistrationStep;
import com.api.rescuemeapi.infrastructure.persistence.repositories.UserRegistrationStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationStateService {

    private final UserRegistrationStateRepository repository;

    public UserRegistrationState getSagaState(UUID sagaId) {
        log.debug("[UserRegistrationSagaStateService::getSagaState] Getting saga state. sagaId={}", sagaId);
        return repository.findById(sagaId)
                .orElseThrow(SagaNotFoundException::new);
    }

    public void markStarted(UUID sagaId,
                            String email,
                            String firstName,
                            String lastName,
                            String phone,
                            String address) {
        log.debug("[UserRegistrationSagaStateService::markStarted] Starting new saga. sagaId={}", sagaId);
        repository.save(
                new UserRegistrationState(sagaId,
                        email,
                        firstName,
                        lastName,
                        phone,
                        address));
    }

    public void markCompleted(UUID sagaId, String token, String refreshToken) {
        log.info("[UserRegistrationSagaStateService::completeSaga] Marking saga as COMPLETED. sagaId={}", sagaId);
        UserRegistrationState state = repository.findById(sagaId)
                .orElseThrow(SagaNotFoundException::new);
        state.markStep(UserRegistrationStep.COMPLETED);
        state.setSuccess(true);
        state.setToken(token);
        state.setRefreshToken(refreshToken);
        repository.save(state);
    }

    public void markFailed(UUID sagaId, Integer status, String errorMessage) {
        log.info("[UserRegistrationSagaStateService::markFailed] Marking saga as FAILED. sagaId={}", sagaId);
        UserRegistrationState state = repository.findById(sagaId)
                .orElseThrow(SagaNotFoundException::new);
        state.markStep(UserRegistrationStep.FAILED);
        state.setSuccess(false);
        state.setStatus(status);
        state.setErrorMessage(errorMessage);
        repository.save(state);
    }

    public UserRegisterSagaResponse getUserRegisterSagaResponse(UUID sagaId) {
        UserRegistrationState state = repository.findById(sagaId).orElse(null);
        if (state == null) {
            return null;
        }
        if (state.getSuccess()) {
            return UserRegisterSagaResponse.builder()
                    .user(UserResponse.builder()
                            .email(state.getEmail())
                            .firstName(state.getFirstName())
                            .lastName(state.getLastName())
                            .phone(state.getPhone())
                            .address(state.getAddress())
                            .build())
                    .success(true)
                    .token(state.getToken())
                    .refreshToken(state.getRefreshToken())
                    .build();
        } else {
            return UserRegisterSagaResponse.builder()
                    .success(false)
                    .status(state.getStatus())
                    .errorMessage(state.getErrorMessage())
                    .build();
        }
    }
}
