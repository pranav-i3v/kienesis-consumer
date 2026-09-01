package com.pranav.kcl.service;

import com.pranav.kcl.config.KinesisConsumerProperties;
import com.pranav.kcl.exception.KinesisConsumerException;
import com.pranav.kcl.model.ConsumerResponse;
import com.pranav.kcl.model.KinesisMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class KinesisConsumerService {

    private final KinesisConsumerProperties properties;
    private final ThreadPoolTaskExecutor executor;
    private final Map<String, Future<?>> activeConsumers = new ConcurrentHashMap<>();

    public KinesisConsumerService(
            KinesisConsumerProperties properties,
            @Qualifier("consumerTaskExecutor") ThreadPoolTaskExecutor executor) {
        this.properties = properties;
        this.executor = executor;
    }

    public List<KinesisMessage> pollRecords(String streamName, int limit) {
        validateStream(streamName);
        int safeLimit = Math.max(1, Math.min(limit, properties.getMaxRecordsPerCall()));
        List<KinesisMessage> records = new ArrayList<>();
        for (int i = 1; i <= safeLimit; i++) {
            records.add(KinesisMessage.of(streamName, "partition-" + i, "payload-" + i));
        }
        return records;
    }

    public ConsumerResponse processRecord(String streamName, String partitionKey, String data) {
        validateStream(streamName);
        if (partitionKey == null || partitionKey.isBlank()) {
            throw new KinesisConsumerException("partitionKey is required");
        }
        KinesisMessage message = KinesisMessage.of(streamName, partitionKey, data);
        return ConsumerResponse.success(streamName, properties.getGroup(), "Processed message: " + message.data());
    }

    public void startConsumer(String streamName, String consumerGroup, ConsumerHandler handler) {
        validateStream(streamName);
        String group = consumerGroup == null || consumerGroup.isBlank() ? properties.getGroup() : consumerGroup;

        Future<?> existing = activeConsumers.get(group);
        if (existing != null && !existing.isDone()) {
            return;
        }

        Future<?> task = executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(properties.getPollIntervalMs());
                    for (KinesisMessage message : pollRecords(streamName, properties.getMaxRecordsPerCall())) {
                        if (handler != null) {
                            handler.handle(message);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        activeConsumers.put(group, task);
    }

    public void stopConsumer(String consumerGroup) {
        String group = consumerGroup == null || consumerGroup.isBlank() ? properties.getGroup() : consumerGroup;
        Future<?> task = activeConsumers.remove(group);
        if (task != null) {
            task.cancel(true);
        }
    }

    private void validateStream(String streamName) {
        if (streamName == null || streamName.isBlank()) {
            throw new KinesisConsumerException("streamName must not be empty");
        }
    }
}
