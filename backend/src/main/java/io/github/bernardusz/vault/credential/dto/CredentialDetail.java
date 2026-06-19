package io.github.bernardusz.vault.credential.dto;

public record CredentialDetail(
  Long id,
  String serviceName,
  String username,
  String description,
  String password
) { }
