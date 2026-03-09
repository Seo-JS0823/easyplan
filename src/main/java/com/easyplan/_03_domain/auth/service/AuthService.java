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
		
		RefreshTokenHash refreshTokenHash = tokenService.hashToken(refreshToken.getValue());
		
		Auth auth = authRepo.findByUserId(userId)
				.orElseGet(() -> AuthDomainFactory.create(userId, subject, refreshTokenHash, refreshToken.getExpiresAt(), now));
		
		auth.updateTokenHash(refreshTokenHash, now);
		
		return authRepo.save(auth);
	}
	
	public Auth rotationRegister(Auth auth, RefreshToken refreshToken) {
		if(auth.getId() == null) {
			throw new AuthException(AuthError.AUTH_NOT_FOUND);
		}
		
		auth.updateTokenHash(tokenService.hashToken(refreshToken.getValue()), clock.now());
		
		return authRepo.save(auth);
	}
	
	public Auth loadAuthByRefreshToken(String refreshToken) {
		RefreshTokenHash tokenHash = tokenService.hashToken(refreshToken);
		
		return authRepo.findByTokenHash(tokenHash.getValue())
				.orElseThrow(() -> new AuthException(AuthError.AUTH_NOT_FOUND));
	}
	
	public TokenPair rotation(Long userId, Subject subject, String role, String refreshToken) {
		RefreshTokenHash rawHash = tokenService.hashToken(refreshToken);
		
		Auth currentAuth = authRepo.findByTokenHash(rawHash.getValue())
				.orElseThrow(() -> new AuthException(AuthError.AUTH_NOT_FOUND));
		
		TokenPair tokens = createTokenPair(currentAuth.getSubject().getValue(), role);
		
		currentAuth.updateTokenHash(RefreshTokenHash.of(tokens.getRefreshToken().getValue()), clock.now());
		
		authRepo.save(currentAuth);
		
		return tokens;
	}
	
}










