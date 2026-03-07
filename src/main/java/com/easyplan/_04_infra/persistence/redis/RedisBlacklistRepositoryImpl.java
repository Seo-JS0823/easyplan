package com.easyplan._04_infra.persistence.redis;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.easyplan._03_domain.auth.model.AccessToken;
import com.easyplan._03_domain.auth.model.RefreshToken;
import com.easyplan._03_domain.auth.repository.BlacklistRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisBlacklistRepositoryImpl implements BlacklistRepository {

	private static final String ACCESS_TOKEN_PREFIX = "BLACK:AT:";
	
	private static final String REFRESH_TOKEN_PREFIX = "BLACK:RT:";
	
	private final StringRedisTemplate redisTemp;
	
	@Override
	public void addBlacklistRefreshToken(RefreshToken refreshToken, Instant now) {
		String key = REFRESH_TOKEN_PREFIX + refreshToken.getValue();
		Duration ttl = calcTtl(refreshToken.getExpiresAt().getValue(), now);
		redisTemp.opsForValue().set(key, "1", ttl);
	}

	@Override
	public boolean isBlacklistRefreshToken(String refreshToken) {
		String key = REFRESH_TOKEN_PREFIX + refreshToken;
		return redisTemp.hasKey(key);
	}

	@Override
	public void addBlacklistAccessToken(AccessToken accessToken, Instant now) {
		String key = ACCESS_TOKEN_PREFIX + accessToken;
		Duration ttl = calcTtl(accessToken.getClaims().getExpiresAt().getValue(), now);
		redisTemp.opsForValue().set(key, "1", ttl);
	}

	@Override
	public boolean isBlacklistAccessToken(String accessToken) {
		String key = ACCESS_TOKEN_PREFIX + accessToken;
		return redisTemp.hasKey(key);
	}
	
	private Duration calcTtl(Instant expiresAt, Instant now) {
		return Duration.between(now, expiresAt);
	}

	@Override
	public void addDefaultTtlBlacklistRefreshToken(String refreshToken) {
		String key = REFRESH_TOKEN_PREFIX + refreshToken;
		Duration ttl = Duration.ofDays(7);
		redisTemp.opsForValue().set(key, "1", ttl);
	}
	
}
