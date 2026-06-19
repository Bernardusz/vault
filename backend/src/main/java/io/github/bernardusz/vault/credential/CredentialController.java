package io.github.bernardusz.vault.credential;

import io.github.bernardusz.vault.credential.dto.*;
import io.github.bernardusz.vault.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {
  private final CredentialService credentialService;
  public CredentialController(CredentialService credentialService) {
    this.credentialService = credentialService;
  }

  @GetMapping
  public ResponseEntity<List<CredentialSummary>> findAll(@AuthenticationPrincipal User currentUser) {
    return ResponseEntity.ok(credentialService.findAll(currentUser.id()));
  }

  @GetMapping("/{id}")
  public  ResponseEntity<CredentialDetail> findById( @PathVariable Long id, @AuthenticationPrincipal User currentUser) {
    return ResponseEntity.ok(credentialService.findById(id, currentUser.id()));
  }

  @GetMapping("/search")
  public ResponseEntity<List<CredentialSummary>> findAllBasedOnSearch(@AuthenticationPrincipal User currentUser, @RequestParam String search) {
    return ResponseEntity.ok(credentialService.findAllBasedOnSearch(currentUser.id(), search));
  }

  @PostMapping
  public ResponseEntity<Void> create(@AuthenticationPrincipal User currentUser, @RequestBody CredentialCreate create) {
    return credentialService.create(create).map(
      id -> ResponseEntity.created(URI.create("/api/credentials/" + id)).<Void>build()
    ).orElse(ResponseEntity.internalServerError().build());
  }

  @PutMapping("/{id}/information")
  public ResponseEntity<Void> updateInformation(@PathVariable Long id, @AuthenticationPrincipal User currentUser, @RequestBody CredentialUpdateInformation updateInformation) {
    credentialService.updateInformation(id, currentUser.id(), updateInformation);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/password")
  public ResponseEntity<Void> updatePassword(@PathVariable Long id, @AuthenticationPrincipal User currentUser, @RequestBody CredentialUpdatePassword updatePassword) {
    credentialService.updatePassword(id, currentUser.id(), updatePassword);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public  ResponseEntity<Void> deleteById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
    credentialService.delete(id, currentUser.id());
    return ResponseEntity.ok().build();
  }
}
