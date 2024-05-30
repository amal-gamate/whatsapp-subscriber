package com.germin8.whatsapp_subscriber.utils;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.germin8.whatsapp_subscriber.webhooks.WebhooksController;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayloadValidator {

//    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static final Logger logger = LoggerFactory.getLogger(WebhooksController.class);

    public static boolean validatePayload(String payload, String signature, String appSecret) {
//        try {
//            String expectedSignature = calculateSignature(payload, appSecret);
//
//            return signature.equals(expectedSignature);
//        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
//            logger.error("NoSuchAlgorithm exists");
//            return false;
//        }
        logger.info("Validating payload: {}", payload);
        return true;
    }

//    private static String calculateSignature(String payload, String appSecret) throws NoSuchAlgorithmException, InvalidKeyException {
//        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
//        SecretKeySpec secretKeySpec = new SecretKeySpec(appSecret.getBytes(), HMAC_SHA1_ALGORITHM);
//        try {
//            mac.init(secretKeySpec);
//        } catch (InvalidKeyException e) {
//            throw new RuntimeException(e);
//        }
//        byte[] digest = mac.doFinal(payload.getBytes());
//        return "sha1=" + Base64.encodeBase64String(digest);
//    }
}
