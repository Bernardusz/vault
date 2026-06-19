package io.github.bernardusz.vault.exception;

import io.github.bernardusz.vault.exception.custom.UserAlreadyExists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserAlreadyExists.class)
  public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExists ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
