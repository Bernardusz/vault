package io.github.bernardusz.vault.auth;

import io.github.bernardusz.vault.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailService implements UserDetailsService {
  private final UserRepository userRepository;

  public  UserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
    return userRepository.findSecurityUserByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
