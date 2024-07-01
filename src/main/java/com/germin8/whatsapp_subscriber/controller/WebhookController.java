package com.germin8.whatsapp_subscriber.controller;

import com.germin8.whatsapp_subscriber.service.WebhookService;
import com.germin8.whatsapp_subscriber.service.WhatsAppWebhookProducer;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.net.MalformedURLException;

@RestController
@RequestMapping(value = "/webhooks")
public class WebhookController {

    @Autowired
    WhatsAppWebhookProducer notificationProducer;

    @Autowired
    private WebhookService webhookService;

    @Value("${app.verify_token:default}")
    private String VERIFY_TOKEN;

    @Value("${app.app_secret:default}")
    private String APP_SECRET;

    @Value("${server.port:default}")
    private String WEBHOOK_PORT;

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostConstruct
    private void init() throws MalformedURLException {
        logger.info("Connection initiated. Webhooks endpoints exposed on port {}", WEBHOOK_PORT);
    }

    @GetMapping
    public ResponseEntity<String> handleWebhookVerification(
        @RequestParam("hub.mode") String mode,
        @RequestParam("hub.challenge") String challenge,
        @RequestParam("hub.verify_token") String hubVerifyToken
    ) {
        return webhookService.handleWebhookVerification(mode, challenge, hubVerifyToken);
    }

    @PostMapping
    public ResponseEntity<String> handleWebhookNotification(@RequestBody String payload, @RequestHeader HttpHeaders headers) {
        String signature = headers.getFirst("X-Hub-Signature");
        return webhookService.handleWebhookNotification(payload, signature);
    }

}
