package io.github.bernardusz.vault.auth.filters;

import io.github.bernardusz.vault.auth.JwtService;
import io.github.bernardusz.vault.auth.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final HandlerExceptionResolver exceptionResolver;
  private final JwtService jwtService;
  private final UserDetailService userDetailService;

  public JwtAuthenticationFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver, JwtService jwtService, UserDetailService userDetailService) {
    this.exceptionResolver = exceptionResolver;
    this.jwtService = jwtService;
    this.userDetailService = userDetailService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authorization = request.getHeader("Authorization");
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    String jwt = authorization.substring(7);
    try{
      String email = jwtService.extractUsername(jwt);
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (email != null && authentication == null) {
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        if (jwtService.validateToken(jwt, userDetails)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
      filterChain.doFilter(request, response);
    }
    catch (Exception e){
      exceptionResolver.resolveException(request, response, null, e);
    }
  }
}
