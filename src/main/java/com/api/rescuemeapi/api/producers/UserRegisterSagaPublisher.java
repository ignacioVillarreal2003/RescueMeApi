package com.api.rescuemeapi.api.producers;

import com.api.rescuemeapi.config.properties.RabbitProperties;
import com.api.rescuemeapi.domain.dtos.user.CompensateUserRegisterCommand;
import com.api.rescuemeapi.domain.dtos.user.UserRegisterCommand;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisterSagaPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    public void publishRegisterUserCommand(UserRegisterCommand message) {
        rabbitTemplate.convertAndSend(
                rabbitProperties.getExchange().getAuth(),
                rabbitProperties.getRoutingKey().getUserRegisterCommand(),
                message
        );
    }

    public void publishCompensateUserRegisterCommand(CompensateUserRegisterCommand message) {
        rabbitTemplate.convertAndSend(
                rabbitProperties.getExchange().getAuth(),
                rabbitProperties.getRoutingKey().getCompensateUserRegisterCommand(),
                message
        );
    }
}
