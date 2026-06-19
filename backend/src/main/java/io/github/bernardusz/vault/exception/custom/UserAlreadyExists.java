package io.github.bernardusz.vault.exception.custom;

public class UserAlreadyExists extends RuntimeException {
  public UserAlreadyExists(String message) {
    super(message);
  }
}
