package com.easy_plan._04_infra.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.easy_plan._03_domain.auth.JwtTokenProvider;
import com.easy_plan._03_domain.auth.model.AccessToken;
import com.easy_plan._03_domain.auth.model.Jti;
import com.easy_plan._03_domain.auth.model.RefreshToken;
import com.easy_plan._03_domain.auth.model.TokenClaims;
import com.easy_plan._03_domain.auth.model.TokenExpiration;
import com.easy_plan._03_domain.auth.model.TokenHash;
import com.easy_plan._05_shared.Clock;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {
	private static final int REFRESH_TOKEN_LENGTH = 64;
	
	private static final String REFRESH_TOKEN_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	private final SecretKey key;
	
	private final Clock clock;
	
	private final long accessTokenExpirationMs = 1000L * 60 * 30;
	
	private final long refreshTokenExpirationMs = 1000L * 60 * 60 * 24 * 7;
	
	private final SecureRandom secureRandom = new SecureRandom();
	
	public JwtTokenProviderImpl(@Value("${jwt.secret}") String secretKey, Clock clock) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		this.clock = clock;
	}

	@Override
	public Long getUserIdFromToken(String token) throws NumberFormatException {
		Claims claims = getClaims(token);
		String userId = claims.getSubject();
		return Long.parseLong(userId);
	}

	@Override
	public Jti getJtiFromToken(String token) {
		Claims claims = getClaims(token);
		return new Jti(claims.get("jti", String.class));
	}

	@Override
	public AccessToken createAccessToken(Long userId, String role) {
		Instant now = clock.now();
		Jti jti = createJti();
		
		Date iss = Date.from(now);
		Date exp = new Date(iss.getTime() + accessTokenExpirationMs);
		TokenExpiration expiration = new TokenExpiration(exp.toInstant());
		
		Claims claims = Jwts.claims()
				.subject(String.valueOf(userId))
				.add("role", role)
				.add("jti", jti.getValue())
				.issuedAt(iss)
				.expiration(exp)
				.build();
		
		String accessToken = Jwts.builder()
				.claims(claims)
				.signWith(key)
				.compact();
		
		return new AccessToken(accessToken, jti, expiration);
	}

	@Override
	public RefreshToken createRefreshToken(Long userId) {
		Instant now = clock.now();
		Jti jti = createJti();
		
		Date iss = Date.from(now);
		Date exp = new Date(iss.getTime() + refreshTokenExpirationMs);
		TokenExpiration expiration = new TokenExpiration(exp.toInstant());
		
		String tokenValue = createSecureRandomToken();
		
		TokenHash tokenHash = hashToken(tokenValue);
		
		return new RefreshToken(tokenValue, jti, tokenHash, expiration);
	}

	@Override
	public TokenClaims extractAccessToken(String accessToken) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(accessToken)
				.getPayload();
		
		Long userId = Long.parseLong(claims.getSubject());
		String role = claims.get("role", String.class);
		Jti jti = new Jti(claims.get("jti", String.class));
		TokenExpiration expiration = new TokenExpiration(claims.getExpiration().toInstant());
		
		return new TokenClaims(userId, role, jti, expiration);
	}

	@Override
	public String validateAccessToken(String accessToken) {
		String[] branch = {"VALID", "EXPIRED", "INVALID"};
		
		try {
			Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken);
			return branch[0];
		} catch (ExpiredJwtException e) {
			return branch[1];
		} catch (SignatureException | UnsupportedJwtException e) {
			return branch[2];
		} catch (MalformedJwtException e) {
			return branch[2];
		} catch (Exception e) {
			return branch[2];
		}
	}

	@Override
	public boolean validateRefreshToken(String tokenValue, TokenHash tokenHash) {
		TokenHash inputHash = hashToken(tokenValue);
		return constantTimeEquals(inputHash.getValue(), tokenHash.getValue());
	}

	@Override
	public Jti createJti() {
		return new Jti(UUID.randomUUID().toString());
	}
	
	private String createSecureRandomToken() {
		StringBuilder token = new StringBuilder(REFRESH_TOKEN_LENGTH);
		for(int i = 0; i < REFRESH_TOKEN_LENGTH; i++) {
			int index = secureRandom.nextInt(REFRESH_TOKEN_ALPHABET.length());
			token.append(REFRESH_TOKEN_ALPHABET.charAt(index));
		}
		return token.toString();
	}
	
	private TokenHash hashToken(String tokenValue) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(tokenValue.getBytes(StandardCharsets.UTF_8));
			String hash = Base64.getEncoder().encodeToString(hashBytes);
			return new TokenHash(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("토큰 해시 생성 실패");
		}
	}
	
	private Claims getClaims(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims;
	}
	
	private boolean constantTimeEquals(String a, String b) {
		if(a.length() != b.length()) {
			return false;
		}
		
		int result = 0;
		for(int i = 0; i < a.length(); i++) {
			result |= a.charAt(i) ^ b.charAt(i);
		}
		return result == 0;
	}
}
