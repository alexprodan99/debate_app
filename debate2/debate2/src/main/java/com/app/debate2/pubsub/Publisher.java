package com.app.debate2.pubsub;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${debater.name}")
    private String debaterName;

    public void publishHarryPotterMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "harry_potter", message, currentMessage -> {
            currentMessage.getMessageProperties().getHeaders().put("sender", debaterName);
            return currentMessage;
        });
    }

    public void publishCookingMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "cooking", message, currentMessage -> {
            currentMessage.getMessageProperties().getHeaders().put("sender", debaterName);
            return currentMessage;
        });
    }
}
