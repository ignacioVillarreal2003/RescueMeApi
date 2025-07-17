package com.api.rescuemeapi.application.saga.orchestrator;

import com.api.rescuemeapi.application.saga.helpers.SagaErrorMapper;
import com.api.rescuemeapi.application.saga.services.UserRegistrationSagaStateService;
import com.api.rescuemeapi.application.services.UserService;
import com.api.rescuemeapi.domain.dtos.user.UserResponse;
import com.api.rescuemeapi.domain.saga.command.UserRegisterCompensationCommand;
import com.api.rescuemeapi.domain.saga.command.UserRegisterConfirmationCommand;
import com.api.rescuemeapi.domain.saga.reply.UserRegisterFailureReply;
import com.api.rescuemeapi.domain.saga.reply.UserRegisterSuccessReply;
import com.api.rescuemeapi.domain.saga.state.UserRegistrationSagaState;
import com.api.rescuemeapi.domain.saga.step.UserRegistrationSagaStep;
import com.api.rescuemeapi.infrastructure.messaging.publisher.UserRegistrationPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationSagaOrchestrator {

    private final UserRegistrationPublisher publisher;
    private final UserRegistrationSagaStateService sagaStateService;
    private final UserService userService;

    public void handleUserRegisterSuccessReply(@Valid UserRegisterSuccessReply reply) {
        UUID sagaId = reply.sagaId();
        log.info("[UserRegistrationSagaOrchestrator::handleUserRegisterSuccessReply] Received saga reply. sagaId={}", sagaId);
        try {
            UserRegistrationSagaState state = sagaStateService.getUserRegistrationSagaState(sagaId);
            UserResponse response = userService.completeRegistration(reply.email(),
                    state.getFirstName(),
                    state.getLastName(),
                    state.getPhone(),
                    state.getAddress());
            sagaStateService.completeSaga(sagaId,
                    reply.email(),
                    reply.token(),
                    reply.refreshToken());
            log.info("[UserRegistrationSagaOrchestrator::handleUserRegisterSuccessReply] User created. sagaId={}, email={}", sagaId, response.getEmail());
            publishUserRegisterConfirmationCommand(sagaId);
        }
        catch (Exception ex) {
            log.warn("[UserRegistrationSagaOrchestrator::handleUserRegisterSuccessReply] Exception caught during user registration. sagaId={}", sagaId, ex);
            SagaErrorMapper.SagaError error = SagaErrorMapper.map(ex);
            publishUserRegisterCompensationCommand(sagaId, error.message());
        }
    }

    private void publishUserRegisterConfirmationCommand(UUID sagaId) {
        log.info("[UserRegistrationSagaOrchestrator::publishUserRegisterConfirmationCommand] Publishing confirmation command. sagaId={}", sagaId);
        publisher.publishUserRegisterConfirmationCommand(
                UserRegisterConfirmationCommand.builder()
                        .sagaId(sagaId)
                        .build());
    }

    private void publishUserRegisterCompensationCommand(UUID sagaId,
                                                        String reason) {
        log.info("[UserRegistrationSagaOrchestrator::publishUserRegisterCompensationCommand] Publishing compensation command. sagaId={}", sagaId);
        publisher.publishUserRegisterCompensationCommand(
                UserRegisterCompensationCommand.builder()
                        .sagaId(sagaId)
                        .reason(reason)
                        .build());
    }

    public void handleUserRegisterFailureReply(@Valid UserRegisterFailureReply reply) {
        UUID sagaId = reply.sagaId();
        log.info("[UserRegistrationSagaOrchestrator::handleUserRegisterFailureReply] Received saga reply. sagaId={}", sagaId);
        try {
            sagaStateService.failSaga(sagaId, reply.status(), reply.message());
            log.info("[UserRegistrationSagaOrchestrator::handleUserRegisterSuccessReply] User not created. sagaId={}", sagaId);
        }
        catch (Exception ex) {
            log.warn("[UserRegistrationSagaOrchestrator::handleUserRegisterSuccessReply] Exception caught during user registration. sagaId={}", sagaId, ex);
        }
    }
}
