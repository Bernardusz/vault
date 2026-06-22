package io.github.bernardusz.vault.user;

import io.github.bernardusz.vault.user.dto.UserCreation;
import io.github.bernardusz.vault.user.dto.UserInformationUpdate;
import io.github.bernardusz.vault.user.dto.UserPasswordUpdate;
import io.github.bernardusz.vault.user.dto.UserPublicInfo;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	private final JdbcClient jdbcClient;

	public UserRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public Optional<User> findSecurityById(Long id) {
		return jdbcClient
			.sql(
				"""
				SELECT * FROM users WHERE id = :id
				"""
			)
			.param("id", id)
			.query(User.class)
			.optional();
	}

	public Optional<User> findSecurityByUsername(String username) {
		return jdbcClient
			.sql(
				"""
				SELECT * FROM users WHERE username = :username
				"""
			)
			.param("username", username)
			.query(User.class)
			.optional();
	}

	public Optional<UserPublicInfo> findById(Long userId) {
		return jdbcClient
			.sql(
				"""
				SELECT id, username, email
				FROM users
				WHERE id = :userId
				"""
			)
			.param("userId", userId)
			.query(UserPublicInfo.class)
			.optional();
	}

	public Optional<UserPublicInfo> findByUsername(String username) {
		return jdbcClient
			.sql(
				"""
				SELECT id, username, email
				FROM users
				WHERE username = :username
				"""
			)
			.param("username", username)
			.query(UserPublicInfo.class)
			.optional();
	}

	public Optional<Long> create(UserCreation userData) {
		return jdbcClient
			.sql(
				"""
				INSERT INTO users
				(username, password, email)
				VALUES
				(:username, :password, :email) RETURNING id
				"""
			)
			.param("username", userData.username())
			.param("password", userData.password())
			.param("email", userData.email())
			.query(Long.class)
			.optional();
	}

	public void updateUserInformation(
		Long userId,
		UserInformationUpdate userData
	) {
		jdbcClient
			.sql(
				"""
				UPDATE users SET
				username = :username,
				email = :email
				WHERE id = :userId
				"""
			)
			.param("username", userData.username())
			.param("email", userData.email())
			.param("userId", userId)
			.update();
	}

	public void updateUserPassword(
		Long userId,
		UserPasswordUpdate userPassword
	) {
		jdbcClient
			.sql(
				"""
				UPDATE users SET
				password = :password
				WHERE id = :userId
				"""
			)
			.param("password", userPassword.password())
			.param("userId", userId)
			.update();
	}

	public void delete(Long userId) {
		jdbcClient
			.sql(
				"""
				DELETE FROM users WHERE id = :userId
				"""
			)
			.param("userId", userId)
			.update();
	}

	public Boolean existsByUsername(String username) {
		return jdbcClient
			.sql(
				"""
				SELECT EXISTS (
				  SELECT 1
				  FROM users
				  WHERE username = :username
				);
				"""
			)
			.param("username", username)
			.query(Boolean.class)
			.single();
	}
}
