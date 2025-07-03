package com.api.rescuemeapi.config;

import com.api.rescuemeapi.config.properties.RabbitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

     private final RabbitProperties rabbitProperties;

    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange(rabbitProperties.getExchange().getAuth());
    }

        @Bean
    public TopicExchange mediaExchange() {
        return new TopicExchange(rabbitProperties.getExchange().getMedia());
    }

    @Bean
    public Queue userRegisterCommandQueue() {
        return new Queue(rabbitProperties.getQueue().getUserRegisterCommand());
    }

    @Bean
    public Queue userRegisterReplyQueue() {
        return new Queue(rabbitProperties.getQueue().getUserRegisterReply());
    }

    @Bean
    public Queue compensateUserRegisterCommandQueue() {
        return new Queue(rabbitProperties.getQueue().getCompensateUserRegisterCommand());
    }

    @Bean
    public Queue createImagesCommandQueue() {
        return new Queue(rabbitProperties.getQueue().getCreateImagesCommand());
    }

    @Bean
    public Queue reorderImagesCommandQueue() {
        return new Queue(rabbitProperties.getQueue().getReorderImagesCommand());
    }

    @Bean
    public Queue deleteImageCommandQueue() {
        return new Queue(rabbitProperties.getQueue().getDeleteImageCommand());
    }

    @Bean
    public Queue deleteImagesByReferenceCommandQueue() {
        return new Queue(rabbitProperties.getQueue().getDeleteImagesByReferenceCommand());
    }

    @Bean
    public Binding bindingUserRegisterCommand() {
        return BindingBuilder
                .bind(userRegisterCommandQueue())
                .to(authExchange())
                .with(rabbitProperties.getRoutingKey().getUserRegisterCommand());
    }

    @Bean
    public Binding bindingUserRegisterReply() {
        return BindingBuilder
                .bind(userRegisterReplyQueue())
                .to(authExchange())
                .with(rabbitProperties.getRoutingKey().getUserRegisterReply());
    }

    @Bean
    public Binding bindingCompensateUserRegisterCommand() {
        return BindingBuilder
                .bind(compensateUserRegisterCommandQueue())
                .to(authExchange())
                .with(rabbitProperties.getRoutingKey().getCompensateUserRegisterCommand());
    }

    @Bean
    public Binding bindingCreateImagesCommand() {
        return BindingBuilder
                .bind(createImagesCommandQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getCreateImagesCommand());
    }

    @Bean
    public Binding bindingReorderImagesCommand() {
        return BindingBuilder
                .bind(reorderImagesCommandQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getReorderImagesCommand());
    }

    @Bean
    public Binding bindingDeleteImageCommand() {
        return BindingBuilder
                .bind(deleteImageCommandQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getDeleteImageCommand());
    }

    @Bean
    public Binding bindingDeleteImagesByReferenceCommand() {
        return BindingBuilder
                .bind(deleteImagesByReferenceCommandQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getDeleteImagesByReferenceCommand());
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
