package com.easyplan._03_domain.auth.repository;

import java.time.Instant;

import com.easyplan._03_domain.auth.model.AccessToken;
import com.easyplan._03_domain.auth.model.RefreshToken;

public interface BlacklistRepository {
	void addBlacklistRefreshToken(RefreshToken refreshToken, Instant now);
	
	void addDefaultTtlBlacklistRefreshToken(String refreshToken);
	
	boolean isBlacklistRefreshToken(String refreshToken);
	
	void addBlacklistAccessToken(AccessToken accessToken, Instant now);
	
	boolean isBlacklistAccessToken(String accessToken);
}
