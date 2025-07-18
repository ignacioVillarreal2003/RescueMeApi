package com.api.rescuemeapi.application.saga.orchestrator;

import com.api.rescuemeapi.application.exceptions.SagaNotFoundException;
import com.api.rescuemeapi.application.saga.helpers.SagaErrorMapper;
import com.api.rescuemeapi.application.saga.services.UserRegistrationStateService;
import com.api.rescuemeapi.application.services.UserService;
import com.api.rescuemeapi.domain.dtos.user.UserResponse;
import com.api.rescuemeapi.domain.saga.command.RollbackUserRegistrationCommand;
import com.api.rescuemeapi.domain.saga.command.ConfirmUserRegistrationCommand;
import com.api.rescuemeapi.domain.saga.reply.FailureUserRegistrationReply;
import com.api.rescuemeapi.domain.saga.reply.SuccessUserRegistrationReply;
import com.api.rescuemeapi.domain.saga.state.UserRegistrationState;
import com.api.rescuemeapi.domain.saga.step.UserRegistrationStep;
import com.api.rescuemeapi.infrastructure.messaging.publisher.UserRegistrationPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationOrchestrator {

    private final UserRegistrationPublisher publisher;
    private final UserRegistrationStateService userRegistrationStateService;
    private final UserService userService;

    public void handleSuccessUserRegistrationReply(@Valid SuccessUserRegistrationReply reply) {
        UUID sagaId = reply.sagaId();
        log.info("[Orchestrator::handleSuccess] Received reply sagaId={}", sagaId);
        UserRegistrationState state;
        try {
            state = userRegistrationStateService.getSagaState(sagaId);
        } catch (SagaNotFoundException e) {
            log.warn("[Orchestrator::handleSuccess] Saga not found, skipping. sagaId={}", sagaId);
            return;
        }
        if (state.getStep() != UserRegistrationStep.STARTED) {
            log.debug("[Orchestrator::handleSuccess] Saga already progressed, skipping. sagaId={}", sagaId);
            return;
        }
        try {
            UserResponse response = userService.completeRegistration(
                    state.getEmail(),
                    state.getFirstName(),
                    state.getLastName(),
                    state.getPhone(),
                    state.getAddress());
            try {
                userRegistrationStateService.markCompleted(sagaId, reply.token(), reply.refreshToken());
            } catch (Exception e) {
                log.error("[Orchestrator] Failed to markCompleted, rolling back user creation. sagaId={}", sagaId, e);
                userService.handleRollbackUserRegistration(state.getEmail());
                userRegistrationStateService.markFailed(sagaId, 500, "Failed to persist saga state");
                publishRollbackUserRegistrationCommand(sagaId);
                return;
            }
            log.info("[Orchestrator] User created successfully. sagaId={}, email={}", sagaId, response.getEmail());
            publishConfirmUserRegistrationCommand(sagaId);
        } catch (Exception ex) {
            log.warn("[Orchestrator] Failed to complete user registration. sagaId={}", sagaId, ex);
            SagaErrorMapper.SagaError error = SagaErrorMapper.map(ex);
            userRegistrationStateService.markFailed(sagaId, error.code(), error.message());
            publishRollbackUserRegistrationCommand(sagaId);
        }
    }


    private void publishConfirmUserRegistrationCommand(UUID sagaId) {
        log.info("[UserRegistrationSagaOrchestrator::publishUserRegisterConfirmationCommand] Publishing confirmation command. sagaId={}", sagaId);
        publisher.publishConfirmUserRegistrationCommand(
                ConfirmUserRegistrationCommand.builder()
                        .sagaId(sagaId)
                        .build());
    }

    private void publishRollbackUserRegistrationCommand(UUID sagaId) {
        log.info("[UserRegistrationSagaOrchestrator::publishUserRegisterCompensationCommand] Publishing compensation command. sagaId={}", sagaId);
        publisher.publishRollbackUserRegistrationCommand(
                RollbackUserRegistrationCommand.builder()
                        .sagaId(sagaId)
                        .build());
    }

    public void handleFailureUserRegistrationReply(@Valid FailureUserRegistrationReply reply) {
        UUID sagaId = reply.sagaId();
        log.info("[UserRegistrationSagaOrchestrator::handleUserRegisterFailureReply] Received saga reply. sagaId={}", sagaId);
        try {
            UserRegistrationState state = userRegistrationStateService.getSagaState(sagaId);
            if (state.getStep() == UserRegistrationStep.STARTED) {
                userRegistrationStateService.markFailed(sagaId, reply.status(), reply.message());
                log.info("[UserRegistrationSagaOrchestrator::handleUserRegisterSuccessReply] User not created. sagaId={}", sagaId);
            }
        }
        catch (Exception ex) {
            log.warn("[UserRegistrationSagaOrchestrator::handleUserRegisterSuccessReply] Exception caught during user registration. sagaId={}", sagaId, ex);
        }
    }

    public void recoverCommand(UUID sagaId) {
        log.info("[UserRegistrationSagaOrchestrator::recoverCommand] Starting recovery for sagaId={}", sagaId);
        try {
            UserRegistrationState state = userRegistrationStateService.getSagaState(sagaId);
            if (state.getStep().equals(UserRegistrationStep.STARTED)) {
                userRegistrationStateService.markFailed(sagaId, 500, "Internal server error");
                log.info("[UserRegistrationSagaOrchestrator::recoverCommand] Saga was in STARTED. Marking as FAILED. sagaId={}", sagaId);
            }
        } catch (Exception ex) {
            log.error("[UserRegistrationSagaOrchestrator::recoverCommand] Recovery failed for sagaId={}", sagaId, ex);
        }
    }
}
