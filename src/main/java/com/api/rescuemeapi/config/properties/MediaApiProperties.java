package com.api.rescuemeapi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "media-api")
@Getter
@Setter
public class MediaApiProperties {
    private String url;
}
