package com.easyplan._04_infra.persistence.orm.auth;

import java.time.Instant;

import com.easyplan._03_domain.auth.model.Auth;
import com.easyplan._03_domain.auth.model.AuthDomainFactory;
import com.easyplan._03_domain.auth.model.RefreshTokenHash;
import com.easyplan._03_domain.auth.model.TokenExpiration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AUTH")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id", unique = true, updatable = false, nullable = false)
	private Long userId;
	
	@Column(name = "token_hash", unique = true, nullable = false)
	private String tokenHash;
	
	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	
	public static AuthEntity create(Auth auth) {
		return new AuthEntity(
				auth.getId(),
				auth.getUserId(),
				auth.getRefreshTokenHash().getValue(),
				auth.getExpiresAt().getValue(),
				auth.getCreatedAt(),
				auth.getUpdatedAt()
		);
	}
	
	public Auth toDomain() {
		return AuthDomainFactory.read(
				id,
				userId,
				RefreshTokenHash.of(tokenHash),
				TokenExpiration.of(expiresAt),
				createdAt,
				updatedAt
		);
	}
	
	public void updateTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}
	
	public void onUpdate(Instant now) {
		this.updatedAt = now;
	}
}
