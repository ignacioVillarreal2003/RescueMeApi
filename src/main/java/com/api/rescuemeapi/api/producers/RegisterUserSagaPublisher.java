package com.api.rescuemeapi.api.producers;

import com.api.rescuemeapi.config.properties.RabbitProperties;
import com.api.rescuemeapi.domain.dtos.user.CompensateRegisterUserCommand;
import com.api.rescuemeapi.domain.dtos.user.RegisterUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUserSagaPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    public void publishRegisterUserCommand(RegisterUserCommand message) {
        rabbitTemplate.convertAndSend(
                rabbitProperties.getExchange().getUserRegister(),
                rabbitProperties.getRoutingKey().getUserRegisterCommand(),
                message
        );
    }

    public void publishCompensateUserRegisterCommand(CompensateRegisterUserCommand message) {
        rabbitTemplate.convertAndSend(
                rabbitProperties.getExchange().getUserRegister(),
                rabbitProperties.getRoutingKey().getCompensateUserRegisterCommand(),
                message
        );
    }
}
