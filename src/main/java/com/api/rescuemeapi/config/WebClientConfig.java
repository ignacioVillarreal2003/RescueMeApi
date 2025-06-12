package com.api.rescuemeapi.config;

import com.api.rescuemeapi.config.properties.MediaApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient mediaApiWebClient(MediaApiProperties mediaApiProperties) {
        return WebClient.builder()
                .baseUrl(mediaApiProperties.getUrl())
                .build();
    }
}
