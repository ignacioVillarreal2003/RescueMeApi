package com.api.rescuemeapi.infrastructure.messaging.publisher;

import com.api.rescuemeapi.config.properties.RabbitProperties;
import com.api.rescuemeapi.domain.saga.command.RollbackUserRegistrationCommand;
import com.api.rescuemeapi.domain.saga.command.ConfirmUserRegistrationCommand;
import com.api.rescuemeapi.domain.saga.command.InitiateUserRegistrationCommand;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    public void publishInitiateUserRegistrationCommand(InitiateUserRegistrationCommand cmd) {
        log.info("[UserRegistrationPublisher::publishUserRegisterInitialCommand] Publishing initial command sagaId={}", cmd.getSagaId());
        rabbitTemplate.convertAndSend(
                rabbitProperties.getExchange().getAuth(),
                rabbitProperties.getRoutingKey().getInitiateUserRegistrationCommand(),
                cmd
        );
        log.info("[UserRegistrationPublisher::publishUserRegisterInitialCommand] Initial command published sagaId={}", cmd.getSagaId());
    }

    public void publishRollbackUserRegistrationCommand(RollbackUserRegistrationCommand cmd) {
        log.info("[UserRegistrationPublisher::publishUserRegisterCompensationCommand] Publishing compensation command sagaId={}", cmd.getSagaId());
        rabbitTemplate.convertAndSend(
                rabbitProperties.getExchange().getAuth(),
                rabbitProperties.getRoutingKey().getRollbackUserRegistrationCommand(),
                cmd
        );
        log.info("[UserRegistrationPublisher::publishUserRegisterCompensationCommand] Compensation command published sagaId={}", cmd.getSagaId());
    }

    public void publishConfirmUserRegistrationCommand(ConfirmUserRegistrationCommand cmd) {
        log.info("[UserRegistrationPublisher::publishUserRegisterConfirmationCommand] Publishing confirmation command sagaId={}", cmd.getSagaId());
        rabbitTemplate.convertAndSend(
                rabbitProperties.getExchange().getAuth(),
                rabbitProperties.getRoutingKey().getConfirmUserRegistrationCommand(),
                cmd
        );
        log.info("[UserRegistrationPublisher::publishUserRegisterConfirmationCommand] Confirmation command published sagaId={}", cmd.getSagaId());
    }
}
