package com.app.debate0.pubsub;

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

    public void publishSportMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "sport", message, currentMessage -> {
            currentMessage.getMessageProperties().getHeaders().put("sender", debaterName);
            return currentMessage;
        });
    }

    public void publishHarryPotterMessage(String message) {
        amqpTemplate.convertAndSend(exchange, "harry_potter", message, currentMessage -> {
            currentMessage.getMessageProperties().getHeaders().put("sender", debaterName);
            return currentMessage;
        });
    }
}
