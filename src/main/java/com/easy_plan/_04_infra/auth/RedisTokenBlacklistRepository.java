package com.easy_plan._04_infra.auth;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.easy_plan._03_domain.auth.TokenBlacklistRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisTokenBlacklistRepository implements TokenBlacklistRepository {
	private static final String KEY_AT_PREFIX = "easy-plan:blacklist:at:";
	
	private static final String KEY_RT_PREFIX = "easy-plan:blacklist:rt:";
	
	private final StringRedisTemplate redis;

	@Override
	public void addBlacklistAccessToken(String accessToken, Duration ttl) {
		String key = KEY_AT_PREFIX + accessToken;
		redis.opsForValue().set(key, "1", ttl);
	}

	@Override
	public void addBlacklistRefreshToken(String tokenValue, Duration ttl) {
		String key = KEY_RT_PREFIX + tokenValue;
		redis.opsForValue().set(key, "1", ttl);
	}

	@Override
	public boolean isBlacklistAccessToken(String accessToken) {
		String key = KEY_AT_PREFIX + accessToken;
		return redis.hasKey(key);
	}

	@Override
	public boolean isBlacklistRefreshToken(String tokenValue) {
		String key = KEY_RT_PREFIX + tokenValue;
		return redis.hasKey(key);
	}

}
