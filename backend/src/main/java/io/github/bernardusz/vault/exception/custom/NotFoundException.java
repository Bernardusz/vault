package io.github.bernardusz.vault.exception.custom;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }
}
