package com.germin8.whatsapp_subscriber.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PayloadValidatorTest {

    @Mock
    private PayloadValidator mockValidator;

    @Test
    public void testValidatePayload_ValidSignature() throws NoSuchAlgorithmException, InvalidKeyException {
        String payload = "payload";
        String appSecret = "app_secret";
        String valid_signature = "sha1=BfOz/+DFCq4+A456nRW0Ek4Aifg=";

        assertTrue(PayloadValidator.validatePayload(payload, valid_signature, appSecret));
    }

    @Test
    public void testValidatePayload_InvalidSignature() throws NoSuchAlgorithmException, InvalidKeyException {
        String payload = "payload";
        String appSecret = "app_secret";
        String invalid_signature = "sha=invalid_signature=";

        assertFalse(PayloadValidator.validatePayload(payload, invalid_signature, appSecret));
    }

    @Test
    public void testValidatePayload_ExceptionHandling() throws NoSuchAlgorithmException, InvalidKeyException {
        String payload = "payload";
        String appSecret = "app_secret";
        String signature = "sha1=BfOz/+DFCq4+A456nRW0Ek4Aifg=";

        doThrow(new NoSuchAlgorithmException("Test")).when(PayloadValidator.class);
        PayloadValidator.validatePayload(payload, signature, appSecret);

        doThrow(new NoSuchAlgorithmException("Test")).when(mockValidator);
        PayloadValidator.validatePayload(payload, signature, appSecret);

        assertThrows(
            NoSuchAlgorithmException.class, 
            () -> PayloadValidator.validatePayload(payload, signature, appSecret)
        );
    }

    @Test
    public void testValidatePayload_EmptyOrNullValues() throws NoSuchAlgorithmException, InvalidKeyException {
        assertFalse(PayloadValidator.validatePayload("", "signature", "appSecret"));
        assertFalse(PayloadValidator.validatePayload("payload", "", "appSecret"));
        assertFalse(PayloadValidator.validatePayload("payload", "signature", ""));
        assertFalse(PayloadValidator.validatePayload(null, "signature", "appSecret"));
        assertFalse(PayloadValidator.validatePayload("payload", null, "appSecret"));
        assertFalse(PayloadValidator.validatePayload("payload", "signature", null));
    }
}
