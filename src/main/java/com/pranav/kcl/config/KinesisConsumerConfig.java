package com.pranav.kcl.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class KinesisConsumerConfig {

    @Bean(name = "consumerTaskExecutor")
    public ThreadPoolTaskExecutor consumerTaskExecutor(KinesisConsumerProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int workerThreads = Math.max(1, properties.getWorkerThreads());
        executor.setCorePoolSize(workerThreads);
        executor.setMaxPoolSize(Math.max(workerThreads, workerThreads * 2));
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("kinesis-consumer-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
