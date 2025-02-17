package com.jeremylee.insta_media_service.consumer;

import com.jeremylee.insta_media_service.adapter.ImageStorageAdapter;
import com.jeremylee.insta_media_service.model.message.ImageUploadMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ImageUploadConsumer implements MessageListener {
    @Autowired
    private ImageStorageAdapter imageStorageAdapter;

    @RabbitListener(queues = "image.upload.queue", ackMode = "AUTO")
    public void receiveMessage(@Payload ImageUploadMessage message) {
        System.out.println( "Received message: " + message.getFileName());

        File file = new File(message.getFilePath());
        if (!file.exists()) {
            System.err.println("File not found: " + message.getFilePath());
            return;
        }

        String storedFileKey = imageStorageAdapter.uploadFile(file, message.getContentType());
        System.out.println("File uploaded successfully: " + storedFileKey);
    }

    @Override
    public void onMessage(Message message) {
        String messageBody = new String(message.getBody());
        System.out.println("Received message: " + messageBody);

        // Process the message (e.g., extract file details and upload)
    }
}
