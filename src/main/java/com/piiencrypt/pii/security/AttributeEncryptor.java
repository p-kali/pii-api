package com.piiencrypt.pii.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.SecureRandom;

@Component
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final String ALGO = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16; // 16 bytes IV
    private final byte[] keyBytes;

    public AttributeEncryptor(@Value("${encryption.secret}") String key) {
        keyBytes = new byte[32]; // AES-256 requires 32 bytes
        byte[] k = key.getBytes();
        System.arraycopy(k, 0, keyBytes, 0, Math.min(k.length, keyBytes.length));
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            // Generate random IV
            byte[] iv = new byte[IV_SIZE];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGO);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));

            byte[] encrypted = cipher.doFinal(attribute.getBytes());

            // Combine IV + ciphertext
            byte[] combined = new byte[IV_SIZE + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, IV_SIZE);
            System.arraycopy(encrypted, 0, combined, IV_SIZE, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            byte[] combined = Base64.getDecoder().decode(dbData);

            byte[] iv = new byte[IV_SIZE];
            byte[] encrypted = new byte[combined.length - IV_SIZE];
            System.arraycopy(combined, 0, iv, 0, IV_SIZE);
            System.arraycopy(combined, IV_SIZE, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(ALGO);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));

            return new String(cipher.doFinal(encrypted));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting", e);
        }
    }
}
