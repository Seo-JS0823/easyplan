package com.easyplan._03_domain.auth.repository;

import java.time.Duration;

import com.easyplan._03_domain.auth.model.AccessToken;
import com.easyplan._03_domain.auth.model.RefreshToken;

public interface BlacklistRepository {
	void addBlacklistRefreshToken(RefreshToken refreshToken, Duration ttl);
	
	boolean isBlacklistRefreshToken(RefreshToken refreshToken);
	
	void addBlacklistAccessToken(AccessToken accessToken, Duration ttl);
	
	boolean isBlacklistAccessToken(AccessToken accessToken);
}
