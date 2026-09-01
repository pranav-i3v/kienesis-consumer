package com.pranav.kcl.service;

import com.pranav.kcl.model.KinesisMessage;

@FunctionalInterface
public interface ConsumerHandler {
    void handle(KinesisMessage message);
}
