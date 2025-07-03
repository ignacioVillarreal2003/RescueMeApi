package com.api.rescuemeapi.api.consumers;

import com.api.rescuemeapi.api.producers.UserRegisterSagaPublisher;
import com.api.rescuemeapi.application.services.UserService;
import com.api.rescuemeapi.domain.dtos.user.CompensateUserRegisterCommand;
import com.api.rescuemeapi.domain.dtos.user.UserRegisterReply;
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
public class UserRegisterSagaConsumer {

    private final UserService userService;
    private final UserRegisterSagaPublisher userRegisterSagaPublisher;
    private final SagaStore sagaStore;

    @RabbitListener(queues = "${rabbit.queue.user-register-reply}")
    public void handleRegisterUserReply(@Valid @Payload UserRegisterReply message) {
        if (message.success()) {
            sagaStore.getInitiateRegisterUser(message.sagaId()).ifPresentOrElse(request -> {
                try {
                    UserResponse response = userService.completeRegistration(request, message);
                    sagaStore.putSuccessRegisterUserSagaCache(message.sagaId(), response, message.token(), message.refreshToken());
                } catch (Exception e) {
                    userRegisterSagaPublisher.publishCompensateUserRegisterCommand(
                            CompensateUserRegisterCommand.builder()
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
