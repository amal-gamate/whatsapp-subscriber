package com.germin8.whatsapp_subscriber.webhooks;

import com.germin8.whatsapp_subscriber.utils.PayloadValidator;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/webhooks")
public class WebhooksController {

    @Value("${app.verify_token}")
    private String verifyToken;

    @Value("${app.app_secret}")
    private String appSecret;

    private static final Logger logger = LoggerFactory.getLogger(WebhooksController.class);

    @GetMapping
    public ResponseEntity<String> handleWebhookVerification(
        @RequestParam("hub.mode") String mode,
        @RequestParam("hub.challenge") String challenge,
        @RequestParam("hub.verify_token") String hubVerifyToken
    ) {
        logger.info("Received a verification request from WhatsApp with mode: {}, challenge: {}, and verify_token: {}", mode, challenge, hubVerifyToken);

        if ("subscribe".equals(mode) && verifyToken.equals(hubVerifyToken)) {
            logger.info("Verification successful");
            return ResponseEntity.ok(challenge);
        } else {
            logger.error("Verification failed");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping
    public ResponseEntity<String> handleWebhookNotification(@RequestBody String payload, @RequestHeader HttpHeaders headers) {
        String signature = headers.getFirst("X-Hub-Signature");

        logger.info("Received a notification from WhatsApp with payload: {} and signature: {}", payload, signature);

        if (signature == null) {
            logger.error("Missing X-Hub-Signature header in webhook request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        boolean isValidPayload = PayloadValidator.validatePayload(payload, signature, appSecret);

        if (isValidPayload) {
            // push payload to kafka
            return ResponseEntity.ok("OK");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/amal")   
    public String blankRequest() {
        return "<h1>WhatsApp Subscriber</h1>";
    }

}
