package io.github.bernardusz.vault.credential;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

  private static final String ALGORITHM = "AES/GCM/NoPadding";
  private static final int TAG_LENGTH_BIT = 128; // Standard authentication tag length
  private static final int IV_LENGTH_BYTE = 12;  // Standard IV length for GCM performance

  @Value("${vault.encryption.key}")
  private String base64SecretKey;

  // Helper to decode your property string into a real Java SecretKey object
  private SecretKey getSecretKey() {
    byte[] decodedKey = Base64.getDecoder().decode(base64SecretKey);
    return new SecretKeySpec(decodedKey, "AES");
  }

  /**
   * 🔒 ENCRYPT: Converts plain text into a secure ciphertext string.
   */
  public String encrypt(String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    // 1. Generate a brand new, highly random IV for this specific entry
    byte[] iv = new byte[IV_LENGTH_BYTE];
    new SecureRandom().nextBytes(iv);

    // 2. Initialize our cipher engine in ENCRYPT mode
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
    cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), parameterSpec);

    // 3. Scramble the bytes
    byte[] cipherText = cipher.doFinal(plainText.getBytes());

    // 4. Combine [IV (12 bytes)] + [CipherText (variable bytes)] into a single array
    byte[] combinedPayload = new byte[iv.length + cipherText.length];
    System.arraycopy(iv, 0, combinedPayload, 0, iv.length);
    System.arraycopy(cipherText, 0, combinedPayload, iv.length, cipherText.length);

    // 5. Encode to Base64 text string so it stores easily in PostgreSQL
    return Base64.getEncoder().encodeToString(combinedPayload);
  }

  /**
   * 🔓 DECRYPT: Converts a stored database ciphertext string back to plain text.
   */
  public String decrypt(String encryptedBase64) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    // 1. Convert the Base64 database text back to raw bytes
    byte[] combinedPayload = Base64.getDecoder().decode(encryptedBase64);

    // 2. Extract the 12-byte IV from the front of the payload
    byte[] iv = new byte[IV_LENGTH_BYTE];
    System.arraycopy(combinedPayload, 0, iv, 0, iv.length);

    // 3. Extract the remaining bytes (the actual ciphertext)
    byte[] cipherText = new byte[combinedPayload.length - IV_LENGTH_BYTE];
    System.arraycopy(combinedPayload, iv.length, cipherText, 0, cipherText.length);

    // 4. Initialize our cipher engine in DECRYPT mode using that exact IV
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
    cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), parameterSpec);

    // 5. Unscramble the bytes and return the human-readable string
    byte[] plainTextBytes = cipher.doFinal(cipherText);
    return new String(plainTextBytes);
  }
}