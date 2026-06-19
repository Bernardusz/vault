package io.github.bernardusz.vault.auth;

import io.github.bernardusz.vault.auth.dto.LoginRequest;
import io.github.bernardusz.vault.auth.dto.LoginResponse;
import io.github.bernardusz.vault.user.dto.UserCreation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<Void> registerUser(@RequestBody UserCreation userCreation) {
    authService.registerUser(userCreation);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    LoginResponse loginData = authService.loginUser(loginRequest);

    ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", loginData.token())
      .httpOnly(true)
      .secure(true)
      .path("/")
      .maxAge(loginData.expiresIn() / 1000)
      .sameSite("Lax")
      .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    return ResponseEntity.ok(loginData);
  }
}
