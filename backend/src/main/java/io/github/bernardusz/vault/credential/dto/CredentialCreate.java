package io.github.bernardusz.vault.credential.dto;

public record CredentialCreate (
  Long userId,
  String serviceName,
  String username,
  String password,
  String description
){ }
