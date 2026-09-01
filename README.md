# Kinesis Consumer Library (KCL)

A reusable Spring-based Kinesis consumer library for microservices. This project follows the same design philosophy as the KPL library: it is dependency-friendly, configuration-driven, and does not expose a standalone application entry point.

## Overview

This library is designed to be imported into other Java services and used to consume records from AWS Kinesis streams. It exposes a small set of service-layer APIs and Spring configuration beans instead of a web app or bootstrapped application runner.

## Goals

- Reusable library, not an application
- Spring configuration via `@Configuration`
- Dynamic stream and consumer group usage
- Asynchronous record processing
- Thread pool tuning for consumers
- Retry and polling configuration
- Simple integration with AWS Kinesis SDK

## Project Structure

```text
kienesis-consumer/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/pranav/kcl/
│   │   │   ├── config/
│   │   │   │   ├── AwsClientConfig.java
│   │   │   │   ├── KinesisConsumerConfig.java
│   │   │   │   ├── KinesisConsumerConfiguration.java
│   │   │   │   └── KinesisConsumerProperties.java
│   │   │   ├── exception/
│   │   │   │   └── KinesisConsumerException.java
│   │   │   ├── model/
│   │   │   │   ├── ConsumerResponse.java
│   │   │   │   └── KinesisMessage.java
│   │   │   ├── service/
│   │   │   │   ├── ConsumerHandler.java
│   │   │   │   └── KinesisConsumerService.java
│   │   │   └── util/
│   │   │       └── JsonUtil.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/pranav/kcl/
│           └── KinesisConsumerConfigurationTests.java
```

## Key Components

### KinesisConsumerConfiguration

This is the main Spring configuration class for the library. It enables property binding for consumer configuration.

### KinesisConsumerProperties

This binds properties with the prefix `aws.kinesis.consumer`.

Example:

```properties
aws.kinesis.region=us-east-1
aws.kinesis.consumer.group=default-group
aws.kinesis.consumer.stream=default-stream
aws.kinesis.consumer.max-records-per-call=1000
aws.kinesis.consumer.poll-interval-ms=1000
aws.kinesis.consumer.worker-threads=4
aws.kinesis.consumer.retry.max-attempts=3
aws.kinesis.consumer.retry.backoff-ms=1000
```

### AwsClientConfig

Creates the AWS Kinesis client `KinesisClient` using the configured region.

### KinesisConsumerConfig

Defines the thread pool used for asynchronous consumer processing.

### KinesisConsumerService

Provides the main consumption API.

Available methods:

```java
List<KinesisMessage> pollRecords(String streamName, int limit)
ConsumerResponse processRecord(String streamName, String partitionKey, String data)
void startConsumer(String streamName, String consumerGroup, ConsumerHandler handler)
void stopConsumer(String consumerGroup)
```

### ConsumerHandler

Functional interface used to process each Kinesis message:

```java
@FunctionalInterface
public interface ConsumerHandler {
    void handle(KinesisMessage message);
}
```

## Example Usage

```java
import com.pranav.kcl.service.KinesisConsumerService;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private final KinesisConsumerService consumerService;

    public OrderConsumer(KinesisConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    public void start() {
        consumerService.startConsumer("orders-stream", "order-consumer-group", message -> {
            System.out.println("Received message: " + message.data());
        });
    }
}
```

## Build and Test

Run tests:

```bash
./mvnw test
```

Use Java 21:

```bash
export JAVA_HOME=/path/to/jdk-21
./mvnw test
```

## Notes

- This project is intentionally structured as a library and not as a Spring Boot application.
- The `application.properties` file is just the default config template for library consumers.
- Kinesis stream names and consumer groups are intentionally passed dynamically, not hardcoded in config.

## Future Enhancements

- Real Kinesis shard polling using KCL worker model
- Checkpointing and lease management
- Dead-letter queue handling
- Retry policies for failed processing
- Metrics and observability
- Batch record processing

## License

This project is currently for internal or learning use and may be adapted for production use as needed.
