package com.easyplan._03_domain.auth.service;

import java.time.Instant;

import com.easyplan._03_domain.auth.exception.AuthError;
import com.easyplan._03_domain.auth.exception.AuthException;
import com.easyplan._03_domain.auth.model.AccessToken;
import com.easyplan._03_domain.auth.model.Auth;
import com.easyplan._03_domain.auth.model.AuthDomainFactory;
import com.easyplan._03_domain.auth.model.RefreshToken;
import com.easyplan._03_domain.auth.model.RefreshTokenHash;
import com.easyplan._03_domain.auth.model.Subject;
import com.easyplan._03_domain.auth.model.TokenClaims;
import com.easyplan._03_domain.auth.model.TokenExpiration;
import com.easyplan._03_domain.auth.model.TokenPair;
import com.easyplan._03_domain.auth.repository.AuthRepository;
import com.easyplan._03_domain.auth.repository.BlacklistRepository;
import com.easyplan.shared.time.Clock;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthService {
	private final AuthRepository authRepo;
	
	private final BlacklistRepository blacklistRepo;
	
	private final TokenService tokenService;
	
	private final Clock clock;
	
	public TokenPair createTokenPair(String publicId, String role) {
		Subject subject = Subject.of(publicId);
		AccessToken accessToken = tokenService.createAccessToken(subject, role);
		RefreshToken refreshToken = tokenService.createRefreshToken();
		
		return new TokenPair(accessToken, refreshToken);
	}
	
	public Auth loginRegister(Long userId, Subject subject, RefreshToken refreshToken) {
		if(userId == null) {
			throw new AuthException(AuthError.INVALID_USER_ID);
		}
		
		Instant now = clock.now();
		
		RefreshTokenHash refreshTokenHash = tokenService.hashToken(refreshToken);
		
		Auth auth = authRepo.findByUserId(userId)
				.orElseGet(() -> AuthDomainFactory.create(userId, subject, refreshTokenHash, refreshToken.getExpiresAt(), now));
		
		auth.updateTokenHash(refreshTokenHash, now);
		
		return authRepo.save(auth);
	}
	
	public TokenPair rotation(Subject subject, String clientAccessToken, String clientRefreshToken) {
		tokenPairIsBlacklist(clientAccessToken, clientRefreshToken);
		
		TokenClaims currentClaims = tokenService.extractClaims(clientAccessToken);
		
		Instant now = clock.now();
		
		Auth currentAuth = authRepo.findByTokenHash(clientRefreshToken)
				.orElseThrow(() -> new AuthException(AuthError.AUTH_NOT_FOUND));
		
		tokenOwnerValidate(subject, currentAuth, clientRefreshToken);
		
		TokenExpiration currentExpiresAt = currentAuth.getExpiresAt();
		
		RefreshToken inputToken = RefreshToken.of(clientRefreshToken, currentExpiresAt);
		
		AccessToken currentAccessToken = AccessToken.of(clientAccessToken, currentClaims);
		
		TokenPair tokenPair = createTokenPair(currentAuth.getSubject().getValue(), currentClaims.getRole());
		
		blacklistRepo.addBlacklistRefreshToken(inputToken, now);
		
		blacklistRepo.addBlacklistAccessToken(currentAccessToken, now);
		
		return tokenPair;
	}
	
	public Auth loadAuthByUserId(Long userId) {
		return authRepo.findByUserId(userId)
				.orElseThrow(() -> new AuthException(AuthError.AUTH_NOT_FOUND));
	}
	
	public Auth loadAuthById(Long id) {
		return authRepo.findById(id)
				.orElseThrow(() -> new AuthException(AuthError.AUTH_NOT_FOUND));
	}
	
	public void addBlacklistRefreshToken(Subject subject, String refreshToken) {
		Auth auth = authRepo.findBySubject(subject.getValue())
				.orElseThrow(() -> new AuthException(AuthError.AUTH_NOT_FOUND));
		
		if(!tokenService.validateRefreshToken(refreshToken, auth.getRefreshTokenHash().getValue())) {
			
			blacklistRepo.addDefaultTtlBlacklistRefreshToken(refreshToken);
			return;
		}
		
		RefreshToken token = RefreshToken.of(refreshToken, auth.getExpiresAt());
		
		blacklistRepo.addBlacklistRefreshToken(token, clock.now());
	}
	
	private void tokenOwnerValidate(Subject subject, Auth auth, String rt) {
		if(!subject.equals(auth.getSubject())) {
			throw new AuthException(AuthError.AUTH_SESSION_NOT_MATCH);
		}
		
		if(!tokenService.validateRefreshToken(rt, auth.getRefreshTokenHash().getValue())) {
			throw new AuthException(AuthError.AUTH_SESSION_NOT_MATCH);
		}
	}
	
	private void tokenPairIsBlacklist(String at, String rt) {
		if(blacklistRepo.isBlacklistRefreshToken(rt)) {
			throw new AuthException(AuthError.USED_REFRESH_TOKEN);
		}
		
		if(blacklistRepo.isBlacklistAccessToken(at)) {
			throw new AuthException(AuthError.USED_ACCESS_TOKEN);
		}
	}
}










