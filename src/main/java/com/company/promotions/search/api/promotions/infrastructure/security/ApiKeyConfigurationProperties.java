package com.company.promotions.search.api.promotions.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "api.security")
public record ApiKeyConfigurationProperties(List<String> keys) {

    public ApiKeyConfigurationProperties {
        if (keys == null || keys.isEmpty()) {
            throw new IllegalArgumentException("At least one API key must be configured");
        }
    }
}
