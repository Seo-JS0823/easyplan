package com.easyplan.infrastructure.redis;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.easyplan.domain.auth.model.AccessToken;
import com.easyplan.domain.auth.model.RefreshToken;
import com.easyplan.domain.auth.repository.BlacklistRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisBlacklistRepositoryImpl implements BlacklistRepository {
	
	private static final String ACCESS_TOKEN_PREFIX = "BLACK:AT:";
	
	private static final String REFRESH_TOKEN_PREFIX = "BLACK:RT:";
	
	private final StringRedisTemplate redisTemp;
	
	@Override
	public void addBlacklistRefreshToken(RefreshToken refreshToken, Duration ttl) {
		System.out.println("TTL: " + ttl);
		String key = REFRESH_TOKEN_PREFIX + refreshToken.getValue();
		redisTemp.opsForValue().set(key, "1", ttl);
	}

	@Override
	public boolean isBlacklistRefreshToken(RefreshToken refreshToken) {
		String key = REFRESH_TOKEN_PREFIX + refreshToken;
		return redisTemp.hasKey(key);
	}

	@Override
	public void addBlacklistAccessToken(AccessToken accessToken, Duration ttl) {
		System.out.println("TTL: " + ttl);
		String key = ACCESS_TOKEN_PREFIX + accessToken;
		redisTemp.opsForValue().set(key, "1", ttl);
	}

	@Override
	public boolean isBlacklistAccessToken(AccessToken accessToken) {
		String key = ACCESS_TOKEN_PREFIX + accessToken;
		return redisTemp.hasKey(key);
	}

}
