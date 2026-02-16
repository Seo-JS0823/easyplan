package com.easyplan.domain.auth.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import com.easyplan.domain.auth.error.AuthSessionErrorCode;
import com.easyplan.domain.auth.error.AuthSessionException;
import com.easyplan.domain.auth.model.AccessToken;
import com.easyplan.domain.auth.model.AuthSession;
import com.easyplan.domain.auth.model.AuthSessionFactory;
import com.easyplan.domain.auth.model.RefreshToken;
import com.easyplan.domain.auth.model.RefreshTokenHash;
import com.easyplan.domain.auth.model.Subject;
import com.easyplan.domain.auth.model.TokenPair;
import com.easyplan.domain.auth.provider.TokenProvider;
import com.easyplan.domain.auth.repository.AuthSessionRepository;
import com.easyplan.domain.auth.repository.BlacklistRepository;
import com.easyplan.domain.user.model.PublicId;
import com.easyplan.domain.user.model.Role;
import com.easyplan.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthSessionService {
	private final AuthSessionRepository authRepo;
	
	private final BlacklistRepository blacklistRepo;
	
	private final TokenProvider tokenProv;
	
	/**
	 * 토큰세트를 새로 생성함 (AccessToken, RefreshToken)
	 * 
	 * @param subject AccessToken의 Subject
	 * @param role AccessToken의 Role
	 * @param now 현재 시간 Instant
	 * @return {@link TokenPair}
	 */
	public TokenPair createTokenPair(Subject subject, Role role, Instant now) {
		AccessToken accessToken = tokenProv.createAccessToken(subject.userPublicId(), role, now);
		
		RefreshToken refreshToken = tokenProv.createRefreshToken(now);
		
		return new TokenPair(accessToken, refreshToken);
	}
	
	public TokenPair createTokenPair(User user, Instant now) {
		Subject subject = Subject.of(user.publicId());
		Role role = user.role();
		
		return createTokenPair(subject, role, now);
	}
	
	public AuthSession loginRefreshToken(User user, String inputRefreshToken, Instant now) {
		if(inputRefreshToken == null) {
			return null;
		}
		
		RefreshToken refreshToken = RefreshToken.ofFromRequest(inputRefreshToken);
		
		if(blacklistRepo.isBlacklistRefreshToken(refreshToken)) {
			throw new AuthSessionException(AuthSessionErrorCode.USED_REFRESH_TOKEN);
		}
		
		Subject subject = Subject.of(user.publicId());
		
		AuthSession authSession = authRepo.findBySubject(subject);
		
		if(!tokenProv.validateRefreshToken(refreshToken, authSession.refreshTokenHash())) {
			throw new AuthSessionException(AuthSessionErrorCode.INVALID_REFRESH_TOKEN);
		} else {
			Instant expiresAt = authSession.expiresAt();
			blacklistRepo.addBlacklistRefreshToken(refreshToken, calculateRemainingTtl(expiresAt, now));
		}
		
		return authSession;
	}
	
	public TokenPair rotation(String accessToken, String refreshToken, Instant now) {
		Map<String, String> user = tokenProv.expiredExtractUserInfo(accessToken);
		
		Subject subject = Subject.of(PublicId.of(user.get("subject")));
		
		AuthSession currentAuthSession = authRepo.findBySubject(subject);
		
		RefreshToken inputRefreshToken = RefreshToken.ofFromRequest(refreshToken);
		RefreshTokenHash currentTokenHash = currentAuthSession.refreshTokenHash();
		
		if(blacklistRepo.isBlacklistRefreshToken(inputRefreshToken)) {
			throw new AuthSessionException(AuthSessionErrorCode.USED_REFRESH_TOKEN);
		}
		
		if(tokenProv.validateRefreshToken(inputRefreshToken, currentTokenHash)) {			
			Instant expiresAt = currentAuthSession.expiresAt();
			blacklistRepo.addBlacklistRefreshToken(inputRefreshToken, calculateRemainingTtl(expiresAt, now));
		}
		
		Role role = Role.valueOf(user.get("role"));
		
		TokenPair newTokens = createTokenPair(subject, role, now);
		
		RefreshTokenHash newTokenHash = tokenProv.hashToken(newTokens.getRefreshToken());
		currentAuthSession.changeRefreshTokenHash(newTokenHash, newTokens.getRefreshToken().getExpiresAt(), now);
		
		authRepo.save(currentAuthSession);
		
		return newTokens;
	}
	
	public AuthSession rotation(Subject subject, Role role, RefreshToken refreshToken, Instant now) {
		AuthSession current = authRepo.findBySubject(subject);
		
		if(blacklistRepo.isBlacklistRefreshToken(refreshToken)) {
			throw new AuthSessionException(AuthSessionErrorCode.USED_REFRESH_TOKEN);
		}
		
		blacklistRepo.addBlacklistRefreshToken(refreshToken, calculateRemainingTtl(refreshToken, now));
		
		RefreshToken newRefreshToken = tokenProv.createRefreshToken(now);
		
		RefreshTokenHash newRefreshTokenHash = tokenProv.hashToken(newRefreshToken);
		
		Instant newRefreshTokenExpiresAt = newRefreshToken.getExpiresAt();
		
		current.changeRefreshTokenHash(newRefreshTokenHash, newRefreshTokenExpiresAt, now);
		
		return authRepo.save(current);
	}
	
	public AuthSession readBySubject(Subject subject) {
		return authRepo.findBySubject(subject);
	}
	
	public RefreshTokenHash hashToken(RefreshToken refreshToken) {
		return tokenProv.hashToken(refreshToken);
	}
	
	public AuthSession register(AuthSession authSession) {
		return authRepo.save(authSession);
	}
	
	public AuthSession register(Subject subject, RefreshTokenHash refreshTokenHash, Instant expiresAt, Instant now) {
		AuthSession authSession = AuthSessionFactory.create(subject, refreshTokenHash, expiresAt, now);
		return authRepo.save(authSession);
	}
	
	private Duration calculateRemainingTtl(Instant expiresAt, Instant now) {
		Duration remaining = Duration.between(now, expiresAt);
		return remaining;
	}
	
	private Duration calculateRemainingTtl(RefreshToken refreshToken, Instant now) {
		Duration remaining = Duration.between(now, refreshToken.getExpiresAt());
		return remaining;
	}
}
