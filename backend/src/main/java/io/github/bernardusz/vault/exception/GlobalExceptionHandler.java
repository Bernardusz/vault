package io.github.bernardusz.vault.exception;

import io.github.bernardusz.vault.exception.custom.DecryptionFailedException;
import io.github.bernardusz.vault.exception.custom.EncryptionFailedException;
import io.github.bernardusz.vault.exception.custom.NotFoundException;
import io.github.bernardusz.vault.exception.custom.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> handleUserAlreadyExists(
		UserAlreadyExistsException ex
	) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(
		NotFoundException ex
	) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(DecryptionFailedException.class)
	public ResponseEntity<String> handleDecryptionFailed(
		DecryptionFailedException ex
	) {
		return ResponseEntity.internalServerError().body(ex.getMessage());
	}

	@ExceptionHandler(EncryptionFailedException.class)
	public ResponseEntity<String> handleEncryptionFailed(
		EncryptionFailedException ex
	) {
		return ResponseEntity.internalServerError().body(ex.getMessage());
	}
}
