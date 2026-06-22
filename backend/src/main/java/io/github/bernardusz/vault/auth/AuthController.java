package io.github.bernardusz.vault.auth;

import io.github.bernardusz.vault.auth.dto.LoginRequest;
import io.github.bernardusz.vault.auth.dto.LoginResponse;
import io.github.bernardusz.vault.user.UserService;
import io.github.bernardusz.vault.user.dto.UserCreation;
import io.github.bernardusz.vault.user.dto.UserPublicInfo;
import jakarta.servlet.http.HttpServletResponse;
import java.net.Authenticator;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final UserService userService;

	public AuthController(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}

	@GetMapping("/me")
	public ResponseEntity<UserPublicInfo> getCurrentUser() {
		// 1. Grab the user details out of the security context populated by your JWT Filter
		Authentication authentication =
			SecurityContextHolder.getContext().getAuthentication();

		String username =
			authentication == null ? "anonymousUser" : authentication.getName();

		return (authentication == null || "anonymousUser".equals(username))
			? ResponseEntity.ok().build()
			: ResponseEntity.ok(userService.findDetailByUsername(username));
	}

	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(
		@RequestBody UserCreation userCreation
	) {
		authService.registerUser(userCreation);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(
		@RequestBody LoginRequest loginRequest,
		HttpServletResponse response
	) {
		LoginResponse loginData = authService.loginUser(loginRequest);

		ResponseCookie cookie = ResponseCookie.from(
			"AUTH-TOKEN",
			loginData.token()
		)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(loginData.expiresIn() / 1000)
			.sameSite("None")
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response) {
		ResponseCookie deleteCookie = ResponseCookie.from("AUTH-TOKEN", "")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(0)
			.sameSite("None")
			.build();
		response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
		return ResponseEntity.ok().build();
	}
}
