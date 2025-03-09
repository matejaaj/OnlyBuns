package com.example.onlybunsbe.infrastructure.messaging;

import com.example.onlybunsbe.Config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPostMessage(String description, String username, String publishTime) {
        String message = String.format(
                "Post eligible for ads: { description: '%s', username: '%s', publishTime: '%s' }",
                description, username, publishTime
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "", message);
    }
}
