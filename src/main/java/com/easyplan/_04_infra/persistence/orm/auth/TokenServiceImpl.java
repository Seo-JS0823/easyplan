package com.easyplan._04_infra.persistence.orm.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.easyplan._03_domain.auth.exception.AuthException;
import com.easyplan._03_domain.auth.model.AccessToken;
import com.easyplan._03_domain.auth.model.RefreshToken;
import com.easyplan._03_domain.auth.model.RefreshTokenHash;
import com.easyplan._03_domain.auth.model.Subject;
import com.easyplan._03_domain.auth.model.TokenClaims;
import com.easyplan._03_domain.auth.model.TokenExpiration;
import com.easyplan._03_domain.auth.model.TokenId;
import com.easyplan._03_domain.auth.service.TokenService;
import com.easyplan.shared.time.Clock;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class TokenServiceImpl implements TokenService {
	private static final int REFRESH_TOKEN_LENGTH = 64;
	
	private static final String REFRESH_TOKEN_MATERIAL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	@Value("${jwt.times.access}")
	private long ACCESS_TOKEN_VALIDITY;
	
	@Value("${jwt.times.refresh}")
	private long REFRESH_TOKEN_VALIDITY;
	
	private final String HASH_ALGORITHM = "SHA-256";
	
	private final String JWT_JTI = "jti";
	
	private final String JWT_ROLE = "rol";
	
	private final SecretKey key;
	
	private final SecureRandom secureRandom = new SecureRandom();
	
	private final Clock clock;
	
	public TokenServiceImpl(@Value("${jwt.secret}") String secretKey, Clock clock) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		this.clock = clock;
	}

	@Override
	public AccessToken createAccessToken(Subject subject, String role) {
		TokenId tokenId = TokenId.create();
		
		Date now = Date.from(clock.now());
		
		Date expiration = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);
		
		Claims claims = Jwts.claims()
				.subject(subject.getValue())
				.add(JWT_JTI, tokenId.getValue())
				.add(JWT_ROLE, role)
				.issuedAt(now)
				.expiration(expiration)
				.build();
		
		String accessToken = Jwts.builder()
				.claims(claims)
				.signWith(key)
				.compact();
		
		TokenClaims tokenClaims = new TokenClaims(
				subject,
				tokenId,
				role,
				TokenExpiration.of(expiration.toInstant())
		);
		
		return AccessToken.of(accessToken, tokenClaims);
	}

	@Override
	public RefreshToken createRefreshToken() {
		StringBuilder tokenBuilder = new StringBuilder(REFRESH_TOKEN_LENGTH);
		for(int i = 0; i < REFRESH_TOKEN_LENGTH; i++) {
			int index = secureRandom.nextInt(REFRESH_TOKEN_MATERIAL.length());
			tokenBuilder.append(REFRESH_TOKEN_MATERIAL.charAt(index));
		}
		
		String token = tokenBuilder.toString();
		
		Instant expiresAt = clock.now().plusMillis(REFRESH_TOKEN_VALIDITY);
		
		return RefreshToken.of(token, TokenExpiration.of(expiresAt));
	}

	@Override
	public RefreshTokenHash hashToken(RefreshToken refreshToken) {
		String token = refreshToken.getValue();
		
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
			String hash = Base64.getEncoder().encodeToString(hashBytes);
			
			return RefreshTokenHash.of(hash);
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Token Hash Create Fail: " + e.getMessage(), e);
		}
	}

	@Override
	public TokenClaims extractClaims(String accessToken) {
		Claims claims = parseClaims(accessToken);
		
		Subject subject = Subject.of(claims.getSubject());
		TokenId tokenId = TokenId.of(claims.get(JWT_JTI, String.class));
		TokenExpiration expiresAt = TokenExpiration.of(claims.getExpiration().toInstant());
		String role = claims.get(JWT_ROLE, String.class);
		
		return new TokenClaims(subject, tokenId, role, expiresAt);
	}
	
	private Claims parseClaims(String accessToken) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(accessToken)
					.getPayload();
			
			return claims;
		} catch(ExpiredJwtException e) {
			throw new AuthException(AuthDataAccessError.EXPIRED);
		} catch(SignatureException | UnsupportedJwtException e) {
			throw new AuthException(AuthDataAccessError.UNSUPPORTED);
		} catch(MalformedJwtException e) {
			throw new AuthException(AuthDataAccessError.MALFORMED);
		} catch(Exception e) {
			throw new AuthException(AuthDataAccessError.PARSING_ERROR);
		}
	}

	@Override
	public boolean validateAccesstoken(String accessToken) {
		try {
			parseClaims(accessToken);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean validateRefreshToken(String rawRefreshToken, String encodedRefreshToken) {
	    String rawHashValue = hashToken(RefreshToken.ofFromCookie(rawRefreshToken)).getValue();
	    return MessageDigest.isEqual(
	        rawHashValue.getBytes(StandardCharsets.UTF_8), 
	        encodedRefreshToken.getBytes(StandardCharsets.UTF_8)
	    );
	}
	
}
