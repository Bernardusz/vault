package io.github.bernardusz.vault.credential;

import io.github.bernardusz.vault.credential.dto.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CredentialRepository {
  private final JdbcClient jdbcClient;
  public CredentialRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public List<CredentialSummary> findAll(Long userId){
    return jdbcClient.sql(
      """
      SELECT id, service_name, username, description
      FROM credentials
      WHERE user_id = :userId
      """
    ).param("userId", userId)
      .query(CredentialSummary.class)
      .list();
  }

  public Optional<CredentialDetail> findById(Long id, Long userId){
    return jdbcClient.sql(
      """
      SELECT id, service_name, username, password, description
      FROM credentials
      WHERE id = :id AND user_id = :userId
      """
    ).param("id", id)
      .param("userId", userId)
      .query(CredentialDetail.class)
      .optional();
  }

  public List<CredentialSummary> findBySearch(String search, Long userId){
    return jdbcClient.sql(
      """
      SELECT id, service_name, username, description
      FROM credentials
      WHERE user_id = :userId AND (username ILIKE :search OR description ILIKE :search OR service_name ILIKE :search)
      ORDER BY service_name ASC, username ASC, description ASC
      """
    ).param("search", "%" + search + "%")
      .param("userId", userId)
      .query(CredentialSummary.class)
      .list();
  }

  public Optional<Long> create (CredentialCreate create){
    return jdbcClient.sql(
      """
      INSERT INTO credentials
      (user_id, service_name, username, description, password)
      VALUES
      (:userId, :serviceName, :username, :description, :password)
      RETURNING id
      """
    ).param("userId", create.userId())
      .param("serviceName", create.serviceName())
      .param("username", create.username())
      .param("description", create.description())
      .param("password", create.password())
      .query(Long.class)
      .optional();
  }

  public void updateInformation(Long id, Long userId, CredentialUpdateInformation information){
    jdbcClient.sql(
      """
      UPDATE credentials SET
        service_name = :serviceName,
        username = :username,
        description = :description
      WHERE id = :id AND user_id = :userId
      """
    ).param("serviceName", information.serviceName())
      .param("username", information.username())
      .param("description", information.description())
      .param("id", id)
      .param("userId", userId)
      .update();
  }

  public void updatePassword(Long id, Long userId, CredentialUpdatePassword information){
    jdbcClient.sql(
      """
      UPDATE credentials SET
        password = :password
      WHERE id = :id AND user_id = :userId
      """
    ).param("id", id)
      .param("userId", userId)
      .param("password", information.password())
      .update();
  }

  public void delete(Long id, Long userId){
    jdbcClient.sql(
      "DELETE FROM credentials WHERE id = :id AND user_id = userId"
    ).param("id", id).param("userId", userId).update();
  }

}
