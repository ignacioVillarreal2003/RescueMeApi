package com.api.rescuemeapi.application.saga.services;

import com.api.rescuemeapi.domain.dtos.user.UserRegisterSagaResponse;
import com.api.rescuemeapi.domain.dtos.user.UserResponse;
import com.api.rescuemeapi.domain.saga.state.UserRegistrationSagaState;
import com.api.rescuemeapi.domain.saga.step.UserRegistrationSagaStep;
import com.api.rescuemeapi.infrastructure.persistence.repositories.UserRegistrationSagaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationSagaStateService {

    private final UserRegistrationSagaRepository registerSagaRepository;

    public UserRegistrationSagaState getUserRegistrationSagaState(UUID sagaId) {
        log.debug("[UserRegistrationSagaStateService::getUserRegistrationSagaState] Getting saga state. sagaId={}", sagaId);
        return registerSagaRepository.findById(sagaId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "caca"));
    }

    public void startSaga(UUID sagaId, String firstName, String lastName, String phone, String address) {
        log.info("[UserRegistrationSagaStateService::startSaga] Starting saga. sagaId={}", sagaId);
        if (!registerSagaRepository.existsById(sagaId)) {
            registerSagaRepository.save(new UserRegistrationSagaState(sagaId, firstName,  lastName, phone, address));
        }
    }

    public void completeSaga(UUID sagaId, String email, String token, String refreshToken) {
        log.info("[UserRegistrationSagaStateService::completeSaga] Marking saga as COMPLETED. sagaId={}", sagaId);
        registerSagaRepository.findById(sagaId).ifPresent(s -> {
            s.markStep(UserRegistrationSagaStep.COMPLETED);
            s.setSuccess(true);
            s.setEmail(email);
            s.setToken(token);
            s.setRefreshToken(refreshToken);
            registerSagaRepository.save(s);
        });
    }

    public void failSaga(UUID sagaId, Integer status, String errorMessage) {
        log.info("[UserRegistrationSagaStateService::failSaga] Marking saga as FAILED. sagaId={}", sagaId);
        registerSagaRepository.findById(sagaId).ifPresent(s -> {
            s.markStep(UserRegistrationSagaStep.FAILED);
            s.setSuccess(false);
            s.setStatus(status);
            s.setErrorMessage(errorMessage);
            registerSagaRepository.save(s);
        });
    }

    public UserRegisterSagaResponse getUserRegisterSagaResponse(UUID sagaId) {
        UserRegistrationSagaState state = registerSagaRepository.findById(sagaId).orElse(null);
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
