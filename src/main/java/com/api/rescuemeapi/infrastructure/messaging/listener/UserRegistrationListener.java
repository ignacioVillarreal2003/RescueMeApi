package com.api.rescuemeapi.infrastructure.messaging.listener;

import com.api.rescuemeapi.application.exceptions.SagaNotFoundException;
import com.api.rescuemeapi.application.saga.orchestrator.UserRegistrationOrchestrator;
import com.api.rescuemeapi.domain.saga.reply.FailureUserRegistrationReply;
import com.api.rescuemeapi.domain.saga.reply.SuccessUserRegistrationReply;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationListener {

    private final UserRegistrationOrchestrator orchestrator;

    @Retryable(
            retryFor = { AmqpException.class, TransientDataAccessException.class, SagaNotFoundException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    @RabbitListener(queues = "${rabbit.queue.success-user-registration-reply}")
    public void handleSuccessUserRegistrationReply(@Valid @Payload SuccessUserRegistrationReply reply) {
        log.info("[UserRegistrationListener::handleUserRegisterSuccessReply] Received command sagaId={}", reply.sagaId());
        orchestrator.handleSuccessUserRegistrationReply(reply);
        log.info("[UserRegistrationListener::handleUserRegisterSuccessReply] Processing finished sagaId={}", reply.sagaId());
    }

    @Recover
    public void recoverSuccessUserRegistrationReply(TransientDataAccessException ex, SuccessUserRegistrationReply reply) {
        log.error("[UserRegistrationListener::recoverInitiateUserRegistrationCommand] Initial registration permanently failed. sagaId={}", reply.sagaId(), ex);
        orchestrator.recoverCommand(reply.sagaId());
    }

    @Retryable(
            retryFor = { AmqpException.class, TransientDataAccessException.class, SagaNotFoundException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    @RabbitListener(queues = "${rabbit.queue.failure-user-registration-reply}")
    public void handleFailureUserRegistrationReply(@Valid @Payload FailureUserRegistrationReply reply) {
        log.info("[UserRegistrationListener::handleUserRegisterFailureReply] Received command sagaId={}", reply.sagaId());
        orchestrator.handleFailureUserRegistrationReply(reply);
        log.info("[UserRegistrationListener::handleUserRegisterFailureReply] Processing finished sagaId={}", reply.sagaId());
    }

    @Recover
    public void recoverFailureUserRegistrationReply(TransientDataAccessException ex, FailureUserRegistrationReply reply) {
        log.error("[UserRegistrationListener::recoverInitiateUserRegistrationCommand] Initial registration permanently failed. sagaId={}", reply.sagaId(), ex);
        orchestrator.recoverCommand(reply.sagaId());
    }
}
