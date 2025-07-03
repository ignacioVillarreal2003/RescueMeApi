package com.api.rescuemeapi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbit")
@Getter
@Setter
public class RabbitProperties {
    private Exchange exchange;
    private Queue queue;
    private RoutingKey routingKey;

    @Getter
    @Setter
    public static class Exchange {
        private String auth;
        private String media;
    }

    @Getter
    @Setter
    public static class Queue {
        private String userRegisterCommand;
        private String userRegisterReply;
        private String compensateUserRegisterCommand;
        private String createImagesCommand;
        private String reorderImagesCommand;
        private String deleteImageCommand;
        private String deleteImagesByReferenceCommand;
    }

    @Getter
    @Setter
    public static class RoutingKey {
        private String userRegisterCommand;
        private String userRegisterReply;
        private String compensateUserRegisterCommand;
        private String createImagesCommand;
        private String reorderImagesCommand;
        private String deleteImageCommand;
        private String deleteImagesByReferenceCommand;
    }
}
