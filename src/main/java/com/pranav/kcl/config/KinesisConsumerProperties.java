package com.pranav.kcl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.kinesis.consumer")
public class KinesisConsumerProperties {

    private String stream = "default-stream";
    private String group = "default-group";
    private String region = "us-east-1";
    private int maxRecordsPerCall = 1000;
    private long pollIntervalMs = 1000L;
    private int workerThreads = 4;
    private Retry retry = new Retry();

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getMaxRecordsPerCall() {
        return maxRecordsPerCall;
    }

    public void setMaxRecordsPerCall(int maxRecordsPerCall) {
        this.maxRecordsPerCall = maxRecordsPerCall;
    }

    public long getPollIntervalMs() {
        return pollIntervalMs;
    }

    public void setPollIntervalMs(long pollIntervalMs) {
        this.pollIntervalMs = pollIntervalMs;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public Retry getRetry() {
        return retry;
    }

    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    public static class Retry {
        private int maxAttempts = 3;
        private long backoffMs = 1000L;

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public long getBackoffMs() {
            return backoffMs;
        }

        public void setBackoffMs(long backoffMs) {
            this.backoffMs = backoffMs;
        }
    }
}
