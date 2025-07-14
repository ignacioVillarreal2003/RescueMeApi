package com.api.rescuemeapi.application.services;

import com.api.rescuemeapi.application.mappers.UserResponseMapper;
import com.api.rescuemeapi.domain.constants.RegisterSagaStep;
import com.api.rescuemeapi.domain.dtos.user.UserRegisterSagaResponse;
import com.api.rescuemeapi.domain.dtos.user.UserResponse;
import com.api.rescuemeapi.domain.models.UserRegisterSaga;
import com.api.rescuemeapi.infrastructure.persistence.repositories.UserRegisterSagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRegisterSagaService {

    private final UserRegisterSagaRepository userRegisterSagaRepository;

    public void startSaga(UUID sagaId, String firstName, String lastName, String phone, String address) {
        if (!userRegisterSagaRepository.existsById(sagaId)) {
            userRegisterSagaRepository.save(new UserRegisterSaga(sagaId,  firstName, lastName, phone, address));
        }
    }

    public void markUserCreated(UUID sagaId, String email, String token, String refreshToken) {
        userRegisterSagaRepository.findById(sagaId).ifPresent(s -> {
            s.setEmail(email);
            s.setToken(token);
            s.setRefreshToken(refreshToken);
            s.setSuccess(true);
            s.markStep(RegisterSagaStep.USER_CREATED);
            userRegisterSagaRepository.save(s);
        });
    }

    public boolean isStepCompleted(UUID sagaId, RegisterSagaStep step) {
        return userRegisterSagaRepository.findById(sagaId)
                .map(s -> s.getStep().ordinal() >= step.ordinal())
                .orElse(false);
    }

    public void completeSaga(UUID sagaId) {
        userRegisterSagaRepository.findById(sagaId).ifPresent(s -> {
            s.markStep(RegisterSagaStep.COMPLETED);
            userRegisterSagaRepository.save(s);
        });
    }

    public void compensateSaga(UUID sagaId, String errorMessage) {
        userRegisterSagaRepository.findById(sagaId).ifPresent(s -> {
            s.setErrorMessage(errorMessage);
            s.setSuccess(false);
            s.markStep(RegisterSagaStep.COMPENSATED);
            userRegisterSagaRepository.save(s);
        });
    }

    public UserRegisterSaga getUserRegisterSaga(UUID sagaId) {
        return userRegisterSagaRepository.getUserRegisterSagaBySagaId(sagaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saga not found"));
    }

    public UserRegisterSagaResponse getUserRegisterSagaResponse(UUID sagaId) {
        UserRegisterSaga userRegisterSaga = getUserRegisterSaga(sagaId);

        if (Boolean.FALSE.equals(userRegisterSaga.getSuccess())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, userRegisterSaga.getErrorMessage());
        }
        else if (Boolean.TRUE.equals(userRegisterSaga.getSuccess())) {
            return UserRegisterSagaResponse.builder()
                    .success(userRegisterSaga.getSuccess())
                    .user(UserResponse.builder()
                            .email(userRegisterSaga.getEmail())
                            .firstName(userRegisterSaga.getFirstName())
                            .lastName(userRegisterSaga.getLastName())
                            .address(userRegisterSaga.getAddress())
                            .phone(userRegisterSaga.getPhone())
                            .build())
                    .token(userRegisterSaga.getToken())
                    .refreshToken(userRegisterSaga.getRefreshToken())
                    .build();
        }
        return UserRegisterSagaResponse.builder()
                .build();
    }
}
