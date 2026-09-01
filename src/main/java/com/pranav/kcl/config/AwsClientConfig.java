package com.pranav.kcl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;

@Configuration
public class AwsClientConfig {

    @Bean
    public KinesisClient kinesisClient(KinesisConsumerProperties properties) {
        return KinesisClient.builder()
                .region(Region.of(properties.getRegion()))
                .build();
    }
}
