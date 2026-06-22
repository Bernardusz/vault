package io.github.bernardusz.vault.credential;

import io.github.bernardusz.vault.credential.dto.*;
import io.github.bernardusz.vault.exception.custom.DecryptionFailedException;
import io.github.bernardusz.vault.exception.custom.EncryptionFailedException;
import io.github.bernardusz.vault.exception.custom.NotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CredentialService {

	private final CredentialRepository credentialRepository;
	private final EncryptionService encryptionService;

	public CredentialService(
		CredentialRepository credentialRepository,
		EncryptionService encryptionService
	) {
		this.credentialRepository = credentialRepository;
		this.encryptionService = encryptionService;
	}

	@Transactional(readOnly = true)
	public List<CredentialSummary> findAll(Long userId) {
		return credentialRepository.findAll(userId);
	}

	@Transactional(readOnly = true)
	public List<CredentialSummary> findAllBasedOnSearch(
		Long userId,
		String search
	) {
		return credentialRepository.findBySearch(search, userId);
	}

	@Transactional(readOnly = true)
	public CredentialDetail findById(Long id, Long userId)
		throws NotFoundException, DecryptionFailedException {
		return credentialRepository
			.findById(id, userId)
			.map(credentialDetail -> {
				String decryptedPassword = null;
				try {
					decryptedPassword = encryptionService.decrypt(
						credentialDetail.password()
					);
				} catch (Exception e) {
					throw new DecryptionFailedException(e.getMessage());
				}

				return new CredentialDetail(
					credentialDetail.id(),
					credentialDetail.serviceName(),
					credentialDetail.username(),
					decryptedPassword,
					credentialDetail.description()
				);
			})
			.orElseThrow(() ->
				new NotFoundException("Credential wasn't found")
			);
	}

	@Transactional
	public Optional<Long> create(CredentialCreate create)
		throws EncryptionFailedException {
		String encryptedPassword = null;
		try {
			encryptedPassword = encryptionService.encrypt(create.password());
		} catch (Exception e) {
			throw new EncryptionFailedException(e.getMessage());
		}

		CredentialCreate credential = new CredentialCreate(
			create.userId(),
			create.serviceName(),
			create.username(),
			encryptedPassword,
			create.description()
		);

		return credentialRepository.create(credential);
	}

	@Transactional
	public void updateInformation(
		Long id,
		Long userId,
		CredentialUpdateInformation credentialUpdateInformation
	) {
		credentialRepository.updateInformation(
			id,
			userId,
			credentialUpdateInformation
		);
	}

	@Transactional
	public void updatePassword(
		Long id,
		Long userId,
		CredentialUpdatePassword information
	) throws EncryptionFailedException {
		String encryptedPassword = null;
		try {
			encryptedPassword = encryptionService.encrypt(
				information.password()
			);
		} catch (Exception e) {
			throw new EncryptionFailedException(e.getMessage());
		}
		credentialRepository.updatePassword(
			id,
			userId,
			new CredentialUpdatePassword(encryptedPassword)
		);
	}

	@Transactional
	public void delete(Long id, Long userId) {
		credentialRepository.delete(id, userId);
	}
}
