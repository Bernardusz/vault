package io.github.bernardusz.vault.credential;

public record Credential(
  Long id,
  Long userId,
  String serviceName,
  String username,
  String password,
  String description
){ }
