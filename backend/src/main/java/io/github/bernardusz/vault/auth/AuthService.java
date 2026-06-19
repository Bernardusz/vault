package io.github.bernardusz.vault.auth;

import io.github.bernardusz.vault.auth.dto.LoginRequest;
import io.github.bernardusz.vault.auth.dto.LoginResponse;
import io.github.bernardusz.vault.exception.custom.UserAlreadyExists;
import io.github.bernardusz.vault.user.User;
import io.github.bernardusz.vault.user.UserRepository;
import io.github.bernardusz.vault.user.dto.UserCreation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  public void registerUser(UserCreation userCreation) {
    if (userRepository.existsByUsername(userCreation.username())){
      throw new UserAlreadyExists("User already exists");
    }
    String hashedPassword = passwordEncoder.encode(userCreation.password());
    userRepository.create(new UserCreation(userCreation.username(), hashedPassword, userCreation.email()));
  }

  public LoginResponse loginUser(LoginRequest loginRequest){
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginRequest.username(),
        loginRequest.password()
      )
    );

    User user = userRepository.findSecurityUserByUsername(loginRequest.username())
      .orElseThrow(
        () -> new UsernameNotFoundException("Username doesn't exist")
      );

    String token = jwtService.generateToken(user);

    return new LoginResponse(token, jwtService.getExpiration());
  }
}
