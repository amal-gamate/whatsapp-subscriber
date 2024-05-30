package com.germin8.whatsapp_subscriber.webhooks;

import com.germin8.whatsapp_subscriber.utils.PayloadValidator;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

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
            @RequestParam("hub.verify_token") String hubVerifyToken) {

        if ("subscribe".equals(mode) && verifyToken.equals(hubVerifyToken)) {
            return ResponseEntity.ok(challenge);
        } else {
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

}
