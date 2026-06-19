package io.github.bernardusz.vault.exception.custom;

public class EncryptionFailedException extends RuntimeException {
  public EncryptionFailedException(String message) {
    super(message);
  }
}
