package com.api.rescuemeapi.api.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.api.rescuemeapi.config.properties.RabbitProperties;
import com.api.rescuemeapi.domain.dtos.image.CreateImagesCommand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateImagesSagaPublisher {

    private final RabbitProperties rabbitProperties;
    private final RabbitTemplate rabbitTemplate;

    public void publishCreateImagesCommand(CreateImagesCommand message) {
        rabbitTemplate.convertAndSend(
            rabbitProperties.getExchange().getMedia(),
            rabbitProperties.getRoutingKey().getCreateImagesCommand(),
            message
        );
    }
}