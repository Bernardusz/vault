package io.github.bernardusz.vault.auth.dto;

public record LoginRequest(
  String username,
  String password
){ }
