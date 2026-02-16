package com.easyplan.domain.auth.model;

import java.time.Instant;

import com.easyplan.common.utils.Require;

public class AuthSession {
	/* User 식별 값: PUBLIC_ID
	 * Unique, Not Null, Length(44)
	 * 
	 * AccessToken의 Subject로 사용됨
	 */
	private final Subject subject;
	
	/* SecureRandom Length 64 의 랜덤 문자열로 생성된 리프레시 토큰
	 * Unique, Not Null, Length(64)
	 * 
	 * 리프레시 토큰의 원문을 SHA-256 해시하여 생성됨.
	 */
	private RefreshTokenHash refreshTokenHash;
	
	/* RefreshToken의 만료시간을 의미함. UTC 기반
	 * Not Null
	 * 
	 * 현재 시간보다 이후의 시간이어야 함
	 */
	private Instant expiresAt;
	
	/* RefreshToken의 생성시각을 의미함. UTC 기반
	 * Not Null
	 * 
	 * 현재 시간보다 이전의 시간일 수 없음.
	 */
	private Instant createdAt;
	
	// ======================= //
	// ===== Constructor ===== //
	// ======================= //
	
	// package-private AuthSessionFactory
	AuthSession(Subject subject, RefreshTokenHash refreshTokenHash, Instant expiresAt, Instant createdAt) {
		this.subject = Require.require(subject, "subject");
		this.refreshTokenHash = Require.require(refreshTokenHash, "refreshTokenHash");
		this.expiresAt = Require.require(expiresAt, "expiresAt");
		this.createdAt = Require.require(createdAt, "createdAt");
	}
	
	// ============================ //
	// ===== Domain Behaviors ===== //
	// ============================ //
	
	/* 리프레시 토큰 변경
	 * 원문을 SHA-256으로 SecureRandom 해시화 한 해시값과
	 * 만료일을 받고 생성일자를 현재 시간으로 업데이트
	 */
	public void changeRefreshTokenHash(RefreshTokenHash refreshTokenHash, Instant expiresAt, Instant createdAt) {
		this.refreshTokenHash = Require.require(refreshTokenHash, "refreshTokenHash");
		this.expiresAt = Require.require(expiresAt, "expiresAt");
		this.createdAt = Require.require(createdAt, "createdAt");
	}
	
	public String getSubject() {
		return this.subject.userPublicId().getValue();
	}
	
	// ========================= //
	// ===== Domain Guards ===== //
	// ========================= //
	
	/**
	 * 만료되지 않은 토큰인지 확인
	 * 
	 * @param now 현재시간 (UTC)
	 * @return 만료된 토큰이면 true
	 */
	public boolean isExpired(Instant now) {
		return this.expiresAt.isBefore(now);
	}
	
	public Subject subject() { return subject; }
	public RefreshTokenHash refreshTokenHash() { return refreshTokenHash; }
	public Instant expiresAt() { return expiresAt; }
	public Instant createdAt() { return createdAt; }
}
