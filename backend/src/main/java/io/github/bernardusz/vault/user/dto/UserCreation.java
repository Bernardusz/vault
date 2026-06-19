package io.github.bernardusz.vault.user.dto;

public record UserCreation(
  String username,
  String password,
  String email
) { }
