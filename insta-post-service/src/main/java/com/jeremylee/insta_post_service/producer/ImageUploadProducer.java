package com.jeremylee.insta_post_service.producer;

import com.jeremylee.insta_post_service.model.message.ImageUploadMessage;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageUploadProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange topicExchange;

    public void sendMessage(ImageUploadMessage message) {
        rabbitTemplate.convertAndSend(topicExchange.getName(), "image.upload.routingKey", message);
        System.out.println("Sent message: " + message.getFileName());
    }
}

