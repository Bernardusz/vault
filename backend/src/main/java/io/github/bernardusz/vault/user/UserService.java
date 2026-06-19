package io.github.bernardusz.vault.user;

import io.github.bernardusz.vault.exception.custom.NotFoundException;
import io.github.bernardusz.vault.user.dto.UserInformationUpdate;
import io.github.bernardusz.vault.user.dto.UserPasswordUpdate;
import io.github.bernardusz.vault.user.dto.UserPublicInfo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserPublicInfo findDetail(Long id) {
    return userRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("User not found"));
  }

  public void updateUserInformation(Long id, UserInformationUpdate userData) {
    userRepository.updateUserInformation(id, userData);
  }

  public void updateUserPassword(Long id, UserPasswordUpdate userData) {
    String newHashedPassword = passwordEncoder.encode(userData.password());
    userRepository.updateUserPassword(id, new UserPasswordUpdate(newHashedPassword));
  }

  public void deleteUser(Long id) {
    userRepository.delete(id);
  }
}
