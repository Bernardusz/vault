package io.github.bernardusz.vault.auth.dto;

public record LoginResponse(
  String token,
  long expiresIn
) { }
