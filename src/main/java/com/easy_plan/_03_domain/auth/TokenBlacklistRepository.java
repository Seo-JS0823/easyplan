package com.easy_plan._03_domain.auth;

import java.time.Duration;

public interface TokenBlacklistRepository {
	void addBlacklistAccessToken(String jti, Duration ttl);
	
	void addBlacklistRefreshToken(String tokenValue, Duration ttl);
	
	boolean isBlacklistAccessToken(String jti);
	
	boolean isBlacklistRefreshToken(String tokenValue);
}
