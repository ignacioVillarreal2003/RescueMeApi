package com.api.rescuemeapi.api.consumers;

import com.api.rescuemeapi.api.producers.RegisterUserSagaPublisher;
import com.api.rescuemeapi.application.services.UserService;
import com.api.rescuemeapi.domain.dtos.user.CompensateRegisterUserCommand;
import com.api.rescuemeapi.domain.dtos.user.RegisterUserReply;
import com.api.rescuemeapi.domain.dtos.user.UserResponse;
import com.api.rescuemeapi.infrastructure.persistence.SagaStore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@Validated
public class RegisterUserSagaConsumer {

    private final UserService userService;
    private final RegisterUserSagaPublisher registrationPublisher;
    private final SagaStore sagaStore;

    @RabbitListener(queues = "${rabbit.queue.user-register-reply}")
    public void handleRegisterUserReply(@Valid @Payload RegisterUserReply message) {
        if (message.success()) {
            sagaStore.getInitiateRegisterUser(message.sagaId()).ifPresentOrElse(request -> {
                try {
                    UserResponse response = userService.completeRegistration(request, message);
                    sagaStore.putSuccessRegisterUserSagaCache(message.sagaId(), response, message.token(), message.refreshToken());
                } catch (Exception e) {
                    registrationPublisher.publishCompensateUserRegisterCommand(
                            CompensateRegisterUserCommand.builder()
                                    .sagaId(message.sagaId())
                                    .userId(message.userId())
                                    .reason(e.getMessage())
                                    .build()
                    );
                    sagaStore.putFailureRegisterUserSagaCache(message.sagaId(), e.getMessage());
                } finally {
                    sagaStore.removeInitiateRegisterUser(message.sagaId());
                }
            }, () -> {
                sagaStore.putFailureRegisterUserSagaCache(message.sagaId(), "Request expired or not found");
            });
        } else {
            sagaStore.removeInitiateRegisterUser(message.sagaId());
            sagaStore.putFailureRegisterUserSagaCache(message.sagaId(), message.errorMessage());
        }
    }
}
