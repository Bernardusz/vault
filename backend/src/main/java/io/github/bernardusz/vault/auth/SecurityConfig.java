package io.github.bernardusz.vault.auth;

import io.github.bernardusz.vault.auth.filters.JwtAuthenticationFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserDetailService userDetailService;

	public SecurityConfig(
		JwtAuthenticationFilter jwtAuthenticationFilter,
		UserDetailService userDetailService
	) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.userDetailService = userDetailService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(authorize ->
				authorize
					.requestMatchers(
						"/api/auth/login",
						"/api/auth/register",
						"/api/auth/logout",
						"/api/auth/me",
						"/error"
					)
					.permitAll()
					.anyRequest()
					.authenticated()
			)
			.authenticationProvider(authenticationProvider())
			.addFilterBefore(
				jwtAuthenticationFilter,
				UsernamePasswordAuthenticationFilter.class
			);

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(
			userDetailService
		);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(
			List.of("http://localhost:5173", "https://localhost:5173")
		);
		configuration.setAllowedMethods(
			List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
		);
		configuration.setAllowedHeaders(
			List.of("Authorization", "Content-Type", "Cookie")
		);
		configuration.setAllowCredentials(true); // Crucial for HttpOnly Cookie transfers

		UrlBasedCorsConfigurationSource source =
			new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationManager authenticationManager(
		AuthenticationConfiguration config
	) throws Exception {
		return config.getAuthenticationManager();
	}
}
