package com.api.rescuemeapi.api.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.api.rescuemeapi.config.properties.RabbitProperties;
import com.api.rescuemeapi.domain.dtos.image.ReorderImagesCommand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReorderImagesSagaPublisher {
    
    private final RabbitProperties rabbitProperties;
    private final RabbitTemplate rabbitTemplate;

    public void publishReorderImagesCommand(ReorderImagesCommand message) {
        rabbitTemplate.convertAndSend(
            rabbitProperties.getExchange().getMedia(),
            rabbitProperties.getRoutingKey().getReorderImagesCommand(),
            message
        );
    }
}
