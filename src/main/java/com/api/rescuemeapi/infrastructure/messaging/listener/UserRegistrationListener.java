package com.api.rescuemeapi.infrastructure.messaging.listener;

import com.api.rescuemeapi.application.saga.orchestrator.UserRegistrationSagaOrchestrator;
import com.api.rescuemeapi.domain.saga.reply.UserRegisterFailureReply;
import com.api.rescuemeapi.domain.saga.reply.UserRegisterSuccessReply;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationListener {

    private final UserRegistrationSagaOrchestrator orchestrator;

    @Retryable(
            retryFor = { Exception.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    @RabbitListener(queues = "${rabbit.queue.user-register-success-reply}")
    public void handleUserRegisterSuccessReply(@Valid @Payload UserRegisterSuccessReply reply) {
        log.info("[UserRegistrationListener::handleUserRegisterSuccessReply] Received command sagaId={}", reply.sagaId());
        orchestrator.handleUserRegisterSuccessReply(reply);
        log.info("[UserRegistrationListener::handleUserRegisterSuccessReply] Processing finished sagaId={}", reply.sagaId());
    }

    @Retryable(
            retryFor = { Exception.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    @RabbitListener(queues = "${rabbit.queue.user-register-failure-reply}")
    public void handleUserRegisterFailureReply(@Valid @Payload UserRegisterFailureReply reply) {
        log.info("[UserRegistrationListener::handleUserRegisterFailureReply] Received command sagaId={}", reply.sagaId());
        orchestrator.handleUserRegisterFailureReply(reply);
        log.info("[UserRegistrationListener::handleUserRegisterFailureReply] Processing finished sagaId={}", reply.sagaId());
    }
}
