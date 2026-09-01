package com.pranav.kcl.model;

import java.time.Instant;

public record ConsumerResponse(
        String streamName,
        String consumerGroup,
        boolean success,
        String message,
        Instant processedAt
) {

    public static ConsumerResponse success(String streamName, String consumerGroup, String message) {
        return new ConsumerResponse(streamName, consumerGroup, true, message, Instant.now());
    }

    public static ConsumerResponse failure(String streamName, String consumerGroup, String message) {
        return new ConsumerResponse(streamName, consumerGroup, false, message, Instant.now());
    }
}
