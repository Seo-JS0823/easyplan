package com.easyplan.infrastructure.persistence.auth.entity;

import java.time.Instant;

import com.easyplan.domain.auth.model.AuthSession;
import com.easyplan.domain.auth.model.AuthSessionFactory;
import com.easyplan.domain.auth.model.RefreshTokenHash;
import com.easyplan.domain.auth.model.Subject;
import com.easyplan.domain.user.model.PublicId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AUTH")
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class AuthSessionEntity {
	@Id
	@Column(name = "subject")
	private String subject;
	
	@Column(name = "token_hash", unique = true, nullable = false, length = 64)
	private String tokenHash;
	
	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;
	
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
	
	public static AuthSessionEntity create(AuthSession auth) {
		return AuthSessionEntity.builder()
				.subject(auth.subject().userPublicId().getValue())
				.tokenHash(auth.refreshTokenHash().getValue())
				.expiresAt(auth.expiresAt())
				.createdAt(auth.createdAt())
				.build();
	}
	
	public AuthSession toDomain() {
		Subject subject = Subject.of(PublicId.of(this.subject));
		RefreshTokenHash tokenHash = RefreshTokenHash.of(this.tokenHash);
		Instant expiresAt = this.expiresAt;
		Instant createdAt = this.createdAt;
		
		return AuthSessionFactory.create(subject, tokenHash, expiresAt, createdAt);
	}
	
	public void apply(AuthSession auth) {
		this.tokenHash = auth.refreshTokenHash().getValue();
		this.expiresAt = auth.expiresAt();
		this.createdAt = auth.createdAt();
	}
}
