package com.pranav.kcl.exception;

public class KinesisConsumerException extends RuntimeException {

    public KinesisConsumerException(String message) {
        super(message);
    }

    public KinesisConsumerException(String message, Throwable cause) {
        super(message, cause);
    }
}
