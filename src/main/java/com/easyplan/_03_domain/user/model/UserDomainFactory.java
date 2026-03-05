package com.easyplan._03_domain.user.model;

import java.time.Instant;

public class UserDomainFactory {
	
	public static User create(Email email, Nickname nickname, PasswordHash passwordHash, Gender gender, Instant now) {
		return new User(
				null,								// id
				PublicId.create(),
				email,							
				nickname,
				passwordHash,
				UserStatus.ACTIVE,
				gender,
				Role.USER,
				now,								// createdAt
				now									// updatedAt
		);
	}
	
	public static User read(
			Long id,
			PublicId publicId,
			Email email,
			Nickname nickname,
			PasswordHash passwordHash,
			UserStatus status,
			Gender gender,
			Role role,
			Instant createdAt,
			Instant updatedAt) {
		return new User(id, publicId, email, nickname, passwordHash, status, gender, role, createdAt, updatedAt);
	}
}
