package com.germin8.whatsapp_subscriber.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.germin8.whatsapp_subscriber.controller.WebhookController;

public class PayloadValidator {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    public static boolean validatePayload(String payload, String signature, String appSecret) {
        logger.info("Validating payload: {}", payload);

        try {
            return signature.equals(expectedSignature(payload, appSecret));
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException: {}", e.getMessage());
            return false;
        } catch (InvalidKeyException e) {
            logger.error("InvalidKeyException: {}", e.getMessage());
            return false;
        }
    }

    private static String expectedSignature(String payload, String appSecret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(appSecret.getBytes(), HMAC_SHA1_ALGORITHM);

        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        
        byte[] digest = mac.doFinal(payload.getBytes());
        return "sha1=" + Base64.getEncoder().encodeToString(digest);
    }
}
