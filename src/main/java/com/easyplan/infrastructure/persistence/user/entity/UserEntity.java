package com.easyplan.infrastructure.persistence.user.entity;

import java.time.Instant;

import com.easyplan.domain.user.model.Email;
import com.easyplan.domain.user.model.Gender;
import com.easyplan.domain.user.model.Nickname;
import com.easyplan.domain.user.model.PasswordHash;
import com.easyplan.domain.user.model.PublicId;
import com.easyplan.domain.user.model.Role;
import com.easyplan.domain.user.model.User;
import com.easyplan.domain.user.model.UserFactory;
import com.easyplan.domain.user.model.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "public_id", unique = true, nullable = false, updatable = false)
	private String publicId;
	
	@Column(name = "email", unique = true, nullable = false, updatable = false)
	private String email;
	
	@Column(name = "password_hash", nullable = false)
	private String password;
	
	@Column(name = "nickname", unique = true, nullable = false)
	private String nickname;
	
	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	
	public static UserEntity create(User user) {
		return UserEntity.builder()
				.publicId(user.publicId().getValue())
				.email(user.email().getValue())
				.password(user.passwordHash().getHashedValue())
				.nickname(user.nickname().getValue())
				.gender(user.gender())
				.role(user.role())
				.status(user.status())
				.createdAt(user.createdAt())
				.updatedAt(user.updatedAt())
				.build();
	}
	
	public User toDomain() {
		PublicId publicId = PublicId.of(this.publicId);
		Email email = Email.of(this.email);
		Nickname nickname = Nickname.of(this.nickname);
		PasswordHash passwordHash = PasswordHash.of(this.password);
		
		return UserFactory.read(publicId, email, passwordHash, nickname, gender, role, status, createdAt, updatedAt);
	}

	public void apply(User user) {
		this.nickname = user.nickname().getValue();
		this.gender = user.gender();
		this.role = user.role();
		this.status = user.status();
		this.updatedAt = user.updatedAt();
		this.password = user.passwordHash().getHashedValue();
	}
	
}
