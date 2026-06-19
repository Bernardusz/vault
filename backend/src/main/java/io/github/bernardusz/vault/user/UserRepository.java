package io.github.bernardusz.vault.user;

import io.github.bernardusz.vault.user.dto.UserCreation;
import io.github.bernardusz.vault.user.dto.UserInformationUpdate;
import io.github.bernardusz.vault.user.dto.UserPasswordUpdate;
import io.github.bernardusz.vault.user.dto.UserPublicInfo;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
  private final JdbcClient jdbcClient;
  public UserRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public Optional<User> findSecurityUserByUsername(String username) {
    return jdbcClient.sql(
      """
      SELECT * FROM users WHERE username = :username
      """
    ).param("username", username)
      .query(User.class)
      .optional();
  }

  public Optional<UserPublicInfo> findByUsername(String username) {
    return jdbcClient.sql(
      """
      SELECT id, username, email
      FROM users
      WHERE username = :username
      """
    ).param("username", username)
      .query(UserPublicInfo.class)
      .optional();
  }

  public Optional<UserPublicInfo> findById(Long id) {
    return jdbcClient.sql(
      """
      SELECT id, username, email
      FROM users
      WHERE id = :id
      """
    ).param("id", id)
      .query(UserPublicInfo.class)
      .optional();
  }

  public Optional<Long> create(UserCreation userData){
    return jdbcClient.sql(
      """
      INSERT INTO users
      (username, password, email)
      VALUES
      (:username, :password, :email) RETURNING id
      """
    ).param("username", userData.username())
      .param("password", userData.password())
      .param("email", userData.email())
      .query(Long.class)
      .optional();
  }

  public void updateUserInformation(Long id, UserInformationUpdate userData){
    jdbcClient.sql(
      """
      UPDATE users SET
      username = :username,
      email = :email
      WHERE id = :id
      """
    ).param("username", userData.username())
      .param("email", userData.email())
      .param("id", id)
      .update();
  }

  public void updateUserPassword(Long id, UserPasswordUpdate userPassword){
    jdbcClient.sql(
      """
      UPDATE users SET
      password = :password
      WHERE id = :id
      """
    ).param("password", userPassword.password())
      .param("id", id)
      .update();
  }

  public void delete(Long id){
    jdbcClient.sql(
      """
      DELETE FROM users WHERE id = :id
      """
    ).param("id", id)
      .update();
  }

  public Boolean existsByUsername(String username) {
    return jdbcClient.sql(
      """
      SELECT EXISTS (
        SELECT 1
        FROM users
        WHERE username = :username
      );
      """
    ).param("username", username)
      .query(Boolean.class)
      .single();
  }

}
