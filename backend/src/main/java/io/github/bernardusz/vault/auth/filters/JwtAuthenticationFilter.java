package io.github.bernardusz.vault.auth.filters;

import io.github.bernardusz.vault.auth.JwtService;
import io.github.bernardusz.vault.auth.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final HandlerExceptionResolver exceptionResolver;
	private final JwtService jwtService;
	private final UserDetailService userDetailService;

	public JwtAuthenticationFilter(
		@Qualifier(
			"handlerExceptionResolver"
		) HandlerExceptionResolver exceptionResolver,
		JwtService jwtService,
		UserDetailService userDetailService
	) {
		this.exceptionResolver = exceptionResolver;
		this.jwtService = jwtService;
		this.userDetailService = userDetailService;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request)
		throws ServletException {
		String path = request.getRequestURI();
		return (
			path.startsWith("/api/auth/login") ||
			path.startsWith("/api/auth/register")
		);
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String jwt = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("AUTH-TOKEN".equals(cookie.getName())) {
					jwt = cookie.getValue();
					break;
				}
			}
		}

		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String userId = jwtService.extractSubjectId(jwt);
			Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();

			if (userId != null && authentication == null) {
				UserDetails userDetails = userDetailService.loadUserById(
					userId
				);
				if (jwtService.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
						);
					authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(
							request
						)
					);
					SecurityContextHolder.getContext().setAuthentication(
						authToken
					);
				}
			}
		} catch (Exception e) {
			exceptionResolver.resolveException(request, response, null, e);
		}
		filterChain.doFilter(request, response);
	}
}
