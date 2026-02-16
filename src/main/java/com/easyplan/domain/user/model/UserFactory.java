package com.easyplan.domain.user.model;

import java.time.Instant;

public class UserFactory {
	/*
	 * User 생성 전용.
	 * 회원가입 시 사용
	 */
	public static User create(
			PublicId publicId, Email email,
			PasswordHash passwordHash, Nickname nickname,
			Gender gender, Instant now
	) {
		return new User(publicId, email, gender, nickname, passwordHash, Role.USER, UserStatus.ACTIVE, now, now);
	}
	
	/*
	 * User 조회 전용
	 * findByEmail, findByPublicId 등으로 조회한 Entity로 도메인 변환시 사용
	 */
	public static User read(
			PublicId publicId, Email email,
			PasswordHash passwordHash, Nickname nickname,
			Gender gender, Role role,
			UserStatus status, Instant createdAt,
			Instant updatedAt
	) {		
		return new User(publicId, email, gender, nickname, passwordHash, role, status, createdAt, updatedAt);
	}
	
}
