package io.github.bernardusz.vault.credential.dto;

public record CredentialSummary(
  Long id,
  String serviceName,
  String username,
  String description
) { }
