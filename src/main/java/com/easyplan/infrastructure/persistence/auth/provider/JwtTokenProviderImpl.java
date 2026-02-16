package com.easyplan.infrastructure.persistence.auth.provider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.easyplan.domain.auth.model.AccessToken;
import com.easyplan.domain.auth.model.RefreshToken;
import com.easyplan.domain.auth.model.RefreshTokenHash;
import com.easyplan.domain.auth.model.TokenId;
import com.easyplan.domain.auth.provider.TokenProvider;
import com.easyplan.domain.user.model.PublicId;
import com.easyplan.domain.user.model.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenProviderImpl implements TokenProvider {
	private static final int REFRESH_TOKEN_LENGTH = 64;
	
	private static final String REFRESH_TOKEN_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	private final long accessTokenMillie = 1000L * 3;
	
	private final long refreshTokenMillie = 1000L * 60 * 60 * 24 * 7;
	
	private final SecretKey key;
	
	private final SecureRandom secureRandom = new SecureRandom();
	
	public JwtTokenProviderImpl(@Value("${jwt.secret}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
	
	@Override
	public AccessToken createAccessToken(PublicId publicId, Role role, Instant nowSeconds) {
		TokenId tokenId = TokenId.create();
		
		Date iss = Date.from(nowSeconds);
		
		Date exp = new Date(iss.getTime() + accessTokenMillie);
		
		Claims claims = Jwts.claims()
				.subject(publicId.getValue())
				.add("jti", tokenId.getValue())
				.add("role", role.name())
				.issuedAt(iss)
				.expiration(exp)
				.build();
		
		String accessToken = Jwts.builder()
				.claims(claims)
				.signWith(key)
				.compact();
				
		return AccessToken.of(accessToken, tokenId, exp.toInstant());
	}
	
	@Override
	public RefreshToken createRefreshToken(Instant now) {
		String token = createSecureRandomToken();
		
		Instant expiresAt = now.plusMillis(refreshTokenMillie);
		return RefreshToken.of(token, expiresAt);
	}
	
	@Override
	public TokenId createTokenId() {
		return TokenId.of(UUID.randomUUID().toString());
	}

	@Override
	public Map<String, String> expiredExtractUserInfo(String accessToken) {
		Map<String, String> info = new HashMap<>();
		
		try {
			Claims claims = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(accessToken)
					.getPayload();
			
			info.put("subject", claims.getSubject());
			info.put("jti", claims.get("jti", String.class));
			info.put("role", claims.get("role", String.class));
			
		} catch (ExpiredJwtException e) {
			Claims claims = e.getClaims();
			
			info.put("subject", claims.getSubject());
			info.put("jti", claims.get("jti", String.class));
			info.put("role", claims.get("role", String.class));
		}
		
		return info;
	}
	@Override
	public TokenId extractTokenId(String accessToken) {
		Claims claims = extractClaims(accessToken);
		String jti = claims.get("jti", String.class);
		return TokenId.of(jti);
	}

	@Override
	public PublicId extractPublicId(String accessToken) {
		Claims claims = extractClaims(accessToken);
		String subject = claims.getSubject();
		return PublicId.of(subject);
	}
	
	// 랜덤 문자열을 SHA-256 해시화
	@Override
	public RefreshTokenHash hashToken(RefreshToken refreshToken) {
		String tokenValue = refreshToken.getValue();
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(tokenValue.getBytes(StandardCharsets.UTF_8));
			String hash = Base64.getEncoder().encodeToString(hashBytes);
			return RefreshTokenHash.of(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Token Hash Create Fail: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean validateRefreshToken(RefreshToken rawToken, RefreshTokenHash persistedTokenHash) {
		RefreshTokenHash rawTokenHash = hashToken(rawToken);
		return constantTimeEquals(rawTokenHash.getValue(), persistedTokenHash.getValue());
	}
	
	/**
	 * AccessToken 에서 Claims 추출
	 * 
	 * @param accessToken
	 * @return
	 */
	private Claims extractClaims(String accessToken) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(accessToken)
					.getPayload();
			return claims;
			
		} catch(ExpiredJwtException e) {
			throw new JwtTokenException(JwtTokenErrorCode.EXPIRED, e);
		} catch(SignatureException | UnsupportedJwtException e) {
			throw new JwtTokenException(JwtTokenErrorCode.UN_SUPPORTED, e);
		} catch(MalformedJwtException e) {
			throw new JwtTokenException(JwtTokenErrorCode.MALFORMED, e);
		} catch(Exception e) {
			throw new JwtTokenException(JwtTokenErrorCode.PARSE_ERROR, e);
		}
	}
	
	/**
	 * 64 자리수 랜덤 문자열 생성
	 * 
	 * @return
	 */
	private String createSecureRandomToken() {
		StringBuilder token = new StringBuilder(REFRESH_TOKEN_LENGTH);
		for(int i = 0; i < REFRESH_TOKEN_LENGTH; i++) {
			int index = secureRandom.nextInt(REFRESH_TOKEN_ALPHABET.length());
			token.append(REFRESH_TOKEN_ALPHABET.charAt(index));
		}
		return token.toString();
	}
	
	/**
	 * 토큰의 길이와 문자를 비교함.
	 * 문자열이 모두 같아야 true 반환
	 * 문자열 길이를 모두 비교하기 때문에 검증 시간이 항상 동일함.
	 * 중간에 문자가 다르더라도 끝까지 비교.
	 * 
	 * @param a RefreshTokenHash value1
	 * @param b RefreshTokenHash value2
	 * @return
	 */
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
