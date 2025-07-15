package com.api.rescuemeapi.api.consumers;

import com.api.rescuemeapi.api.producers.UserRegisterSagaPublisher;
import com.api.rescuemeapi.application.services.UserRegisterSagaService;
import com.api.rescuemeapi.application.services.UserService;
import com.api.rescuemeapi.domain.constants.RegisterSagaStep;
import com.api.rescuemeapi.domain.dtos.user.CompensateUserRegisterCommand;
import com.api.rescuemeapi.domain.dtos.user.UserRegisterReply;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@Validated
public class UserRegisterSagaConsumer {

    private final UserService userService;
    private final UserRegisterSagaPublisher userRegisterSagaPublisher;
    private final UserRegisterSagaService userRegisterSagaService;

    @RabbitListener(queues = "${rabbit.queue.user-register-reply}")
    public void handleRegisterUserReply(@Valid @Payload UserRegisterReply message) {
        if (userRegisterSagaService.isStepCompleted(message.sagaId(), RegisterSagaStep.USER_CREATED)) {
            return;
        }

        if (message.success()) {
            userRegisterSagaService.markUserCreated(message.sagaId(), message.email(), message.token(), message.refreshToken());

            try {
                userService.completeRegistration(message);
                userRegisterSagaService.completeSaga(message.sagaId());
            } catch (Exception e) {
                userRegisterSagaPublisher.publishCompensateUserRegisterCommand(
                        CompensateUserRegisterCommand.builder()
                                .sagaId(message.sagaId())
                                .reason(e.getMessage())
                                .build()
                );

                userRegisterSagaService.compensateSaga(message.sagaId(), message.errorMessage());
            }
        }
    }
}
