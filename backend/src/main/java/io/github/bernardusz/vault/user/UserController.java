package io.github.bernardusz.vault.user;

import io.github.bernardusz.vault.user.dto.UserInformationUpdate;
import io.github.bernardusz.vault.user.dto.UserPasswordUpdate;
import io.github.bernardusz.vault.user.dto.UserPublicInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/detail")
	public ResponseEntity<UserPublicInfo> findDetail(
		@AuthenticationPrincipal User currentUser
	) {
		return ResponseEntity.ok(userService.findDetail(currentUser.id()));
	}

	@PutMapping("/update/information")
	public ResponseEntity<Void> updateInformation(
		@AuthenticationPrincipal User currentUser,
		@RequestBody UserInformationUpdate userData
	) {
		userService.updateUserInformation(currentUser.id(), userData);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/update/password")
	public ResponseEntity<Void> updatePassword(
		@AuthenticationPrincipal User currentUser,
		@RequestBody UserPasswordUpdate userData
	) {
		userService.updateUserPassword(currentUser.id(), userData);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteUser(
		@AuthenticationPrincipal User currentUser
	) {
		userService.deleteUser(currentUser.id());
		return ResponseEntity.ok().build();
	}
}
