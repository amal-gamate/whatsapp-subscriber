package com.germin8.whatsapp_subscriber.service;

import com.germin8.whatsapp_subscriber.util.PayloadValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    @Autowired
    private WhatsAppWebhookProducer notificationProducer;

    @Value("${app.app_secret:default}")
    private String APP_SECRET;

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);

    public ResponseEntity<String> handleWebhookVerification(String mode, String challenge, String hubVerifyToken) {
        if (!"subscribe".equals(mode)) {
            logger.error("Verification failed: Invalid mode '{}'", mode);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid mode");
        }

        if (!APP_SECRET.equals(hubVerifyToken)) {
            logger.error("Verification failed: Invalid hubVerifyToken '{}'", hubVerifyToken);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid verification token");
        }

        logger.info("Verification successful");
        return ResponseEntity.ok(challenge);
    }

    public ResponseEntity<String> handleWebhookNotification(String payload, String signature) {
        if (signature == null) {
            logger.error("Missing X-Hub-Signature header in webhook request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        boolean isValidPayload = PayloadValidator.validatePayload(payload, signature, APP_SECRET);

        if (isValidPayload) {
            notificationProducer.sendNotification(payload);
            return ResponseEntity.ok("OK");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
