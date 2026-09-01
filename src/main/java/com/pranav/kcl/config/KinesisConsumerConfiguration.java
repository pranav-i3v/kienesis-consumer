package com.pranav.kcl.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KinesisConsumerProperties.class)
public class KinesisConsumerConfiguration {
}
