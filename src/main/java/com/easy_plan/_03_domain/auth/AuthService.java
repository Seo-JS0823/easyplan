package com.easy_plan._03_domain.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import com.easy_plan._03_domain.auth.model.AccessToken;
import com.easy_plan._03_domain.auth.model.Auth;
import com.easy_plan._03_domain.auth.model.RefreshToken;
import com.easy_plan._03_domain.auth.model.TokenClaims;
import com.easy_plan._05_shared.Clock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final AuthRepository authRepo;
	
	private final JwtTokenProvider tokenProvider;
	
	private final TokenBlacklistRepository blacklistRepo;
	
	private final Clock clock;
	
	public void updateOrCreateAuth(Long userId, RefreshToken refreshToken) {
		Auth domain = authRepo.findAuthByUserId(userId).orElse(null);
		
		if(domain == null) {
			domain = Auth.from(userId, refreshToken, clock.now());
			authRepo.saveAuth(domain);
			return;
		}
		
		domain.rotationRefreshToken(refreshToken);
		authRepo.saveAuth(domain);
	}
	
	// 액세스 토큰 생성
	public AccessToken createAccessToken(Long userId, String role) {
		return tokenProvider.createAccessToken(userId, role);
	}
	
	// 리프레시 토큰 생성
	public RefreshToken createRefreshToken(Long userId) {
		return tokenProvider.createRefreshToken(userId);
	}
	
	// 액세스 토큰 블랙리스트 추가
	public void addBlacklistAccessToken(String accessToken) {
		if(accessToken == null) {
			log.debug("AuthService.class addBlacklistAccessToken Parameter Is Null");
			return;
		}
		// 토큰 원문으로 토큰 정보 추출
		TokenClaims claims = tokenProvider.extractAccessToken(accessToken);
		
		// 블랙리스트에 넣을 JTI와 TTL계산
		String jti = claims.getJti().getValue();
		Duration exp = Duration.between(clock.now(), claims.getExpiration().getValue());
		
		blacklistRepo.addBlacklistAccessToken(jti, exp);
	}
	
	// 리프레시 토큰 블랙리스트 추가
	public void addBlacklistRefreshToken(Long userId, String tokenValue) {
		if(userId == null || tokenValue == null) {
			log.debug("AuthService.class addBlacklistRefreshToken Parameter Is Null");
			return;
		}
		
		// UserId로 Auth 조회
		Auth auth = authRepo.findAuthByUserId(userId).orElse(null);
		
		// 조회 결과가 없어도 요청 리프레시 토큰 블랙리스트 추가
		if(auth == null) {
			blacklistRepo.addBlacklistRefreshToken(tokenValue, Duration.ofDays(7));
			return;
		}
		
		// 토큰 검증
		boolean validate = tokenProvider.validateRefreshToken(tokenValue, auth.getTokenHash());
		if(!validate) {
			System.out.println("블랙리스트 검증 실패로 토큰을 저장하지 않습니다.");
			return;
		}
		
		// TTL 계산
		Instant expiration = auth.getExpiresAt().getValue();
		
		blacklistRepo.addBlacklistRefreshToken(tokenValue, Duration.between(clock.now(), expiration));
	}
	
	// 액세스 토큰 블랙리스트 확인
	public boolean isBlacklistAccessToken(String accessToken) {
		String jti = tokenProvider.getJtiFromToken(accessToken).getValue();
		return blacklistRepo.isBlacklistAccessToken(jti);
	}
	
	// 리프레시 토큰 블랙리스트 확인
	public boolean isBlacklistRefreshToken(String tokenValue) {
		return blacklistRepo.isBlacklistRefreshToken(tokenValue);
	}
	
	// UserId로 Auth 조회
	public Optional<Auth> findByUserId(Long userId) {
		return authRepo.findAuthByUserId(userId);
	}
	
	// 어플리케이션 계층으로 전달할 토큰세트
	public static class TokenPair {
		private final AccessToken accessToken;
		
		private final RefreshToken refreshToken;
		
		public TokenPair(AccessToken accessToken, RefreshToken refreshToken) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}
		
		public AccessToken getAccessToken() { return accessToken; }
		
		public RefreshToken getRefreshToken() { return refreshToken; }
	}
}
