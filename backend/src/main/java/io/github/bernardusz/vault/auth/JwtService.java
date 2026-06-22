package io.github.bernardusz.vault.auth;

import io.github.bernardusz.vault.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

	public boolean validateToken(String token, UserDetails user) {
		String extractedUsername = extractSubjectId(token);
		User userDetails = (User) user;

		return (
			extractedUsername.equals(String.valueOf(userDetails.id())) &&
			!isTokenExpired(token)
		);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails user) {
		return buildToken(new HashMap<>(), user);
	}

	public String generateToken(
		UserDetails user,
		Map<String, Object> extraClaims
	) {
		return buildToken(extraClaims, user);
	}

	public String buildToken(
		Map<String, Object> extraClaims,
		UserDetails userDetails
	) {
		User user = (User) userDetails;

		return Jwts.builder()
			.setClaims(extraClaims)
			.setSubject(String.valueOf(user.id()))
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	public String extractSubjectId(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(
		String token,
		Function<Claims, T> claimsResolver
	) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public Key getSigningKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	public long getExpiration() {
		return expiration;
	}
}
