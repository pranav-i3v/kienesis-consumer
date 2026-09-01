package com.pranav.kcl.model;

import java.time.Instant;

public record KinesisMessage(
        String streamName,
        String partitionKey,
        String data,
        String sequenceNumber,
        Instant timestamp
) {

    public static KinesisMessage of(String streamName, String partitionKey, String data) {
        return new KinesisMessage(streamName, partitionKey, data, null, Instant.now());
    }
}
