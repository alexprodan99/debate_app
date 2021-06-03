package com.app.debate0.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Subscriber {
    private Logger logger = LoggerFactory.getLogger(Subscriber.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${debater.name}")
    private String debaterName;

    @RabbitListener(queues = "${rabbitmq.queue.0}")
    public void receiveSportMessage(Message message) {
        List<String> messageContent = getMessageComponents(message);
        String sender = messageContent.get(0);
        String text = messageContent.get(1);
        if(!debaterName.equals(sender)) {
            logger.info("<<sport>> " + sender + ":" + text);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.1}")
    public void receiveHarryPotterMessage(Message message) {
        List<String> messageContent = getMessageComponents(message);

        String sender = messageContent.get(0);
        String text = messageContent.get(1);
        if(!debaterName.equals(sender)) {
            logger.info("<<harry_potter>> " + sender + ":" + text);
        }
    }

    private List<String> getMessageComponents(Message message) {
        String messageContent = (String) rabbitTemplate.getMessageConverter().fromMessage(message);

        String messageHeader = null;
        try {
            messageHeader = (String) message.getMessageProperties().getHeaders().values().toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.info("There is no header for message!");
        }

        List<String> result = new ArrayList<>();
        result.add(messageHeader);
        result.add(messageContent);

        return result;
    }
}
