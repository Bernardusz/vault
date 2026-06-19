package io.github.bernardusz.vault.user.dto;

public record UserPublicInfo(
  Long id,
  String username,
  String email
){ }
