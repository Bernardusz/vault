package io.github.bernardusz.vault.exception.custom;

public class DecryptionFailedException extends RuntimeException {
  public DecryptionFailedException(String message) {
    super(message);
  }
}
