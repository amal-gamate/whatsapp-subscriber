package com.germin8.whatsapp_subscriber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppWebhookProducer {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.notification-topic}")
    private String topic;

    public void sendNotification(String notification) {
        kafkaTemplate.send(topic, notification);
    }

}