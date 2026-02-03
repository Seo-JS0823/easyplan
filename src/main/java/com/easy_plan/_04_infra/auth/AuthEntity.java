package com.easy_plan._04_infra.auth;

import java.time.Instant;

import com.easy_plan._03_domain.auth.model.Auth;
import com.easy_plan._03_domain.auth.model.Jti;
import com.easy_plan._03_domain.auth.model.TokenExpiration;
import com.easy_plan._03_domain.auth.model.TokenHash;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AUTH", indexes = {
	@Index(name = "idx_auth_user_id", columnList = "user_id"),
	@Index(name = "idx_auth_jti", columnList = "jti"),
	@Index(name = "idx_auth_expires", columnList = "expires_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AuthEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_id")
	private Long id;
	
	@Column(name = "user_id", unique = true, nullable = false)
	private Long userId;
	
	@Column(name = "jti", unique = true, nullable = false, length = 64)
	private String jti;
	
	@Column(name = "token_hash", unique = true, nullable = false, length = 50)
	private String tokenHash;
	
	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	public static AuthEntity from(Auth auth) {
		return AuthEntity.builder()
				.id(auth.getAuthId())
				.userId(auth.getUserId())
				.jti(auth.getJti().getValue())
				.tokenHash(auth.getTokenHash().getValue())
				.expiresAt(auth.getExpiresAt().getValue())
				.createdAt(auth.getCreatedAt())
				.build();
	}
	
	public Auth toDomain() {
		return Auth.builder()
			.authId(id)
			.userId(userId)
			.jti(new Jti(jti))
			.tokenHash(new TokenHash(tokenHash))
			.expiresAt(new TokenExpiration(expiresAt))
			.createdAt(createdAt)
			.build();
	}
}	
