package com.api.rescuemeapi.api.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.api.rescuemeapi.config.properties.RabbitProperties;
import com.api.rescuemeapi.domain.dtos.image.DeleteImagesByReferenceCommand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteImagesByReferenceSagaPublisher {
    
    private final RabbitProperties rabbitProperties;
    private final RabbitTemplate rabbitTemplate;

    public void publishDeleteImagesByReferenceCommand(DeleteImagesByReferenceCommand message) {
        rabbitTemplate.convertAndSend(
            rabbitProperties.getExchange().getMedia(),
            rabbitProperties.getRoutingKey().getDeleteImagesByReferenceCommand(),
            message
        );
    }
}
