package com.easyplan._04_infra.persistence.orm.user;

import java.time.Instant;

import com.easyplan._03_domain.user.model.Email;
import com.easyplan._03_domain.user.model.Gender;
import com.easyplan._03_domain.user.model.Nickname;
import com.easyplan._03_domain.user.model.PasswordHash;
import com.easyplan._03_domain.user.model.PublicId;
import com.easyplan._03_domain.user.model.Role;
import com.easyplan._03_domain.user.model.User;
import com.easyplan._03_domain.user.model.UserDomainFactory;
import com.easyplan._03_domain.user.model.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "public_id", unique = true, nullable = false, updatable = false, length = 40)
	private String publicId;
	
	@Column(name = "nickname", unique = true, nullable = false, length = 10)
	private String nickname;
	
	@Column(name = "email", unique = true, nullable = false, updatable = false)
	private String email;
	
	@Column(name = "password_hash", unique = true, nullable = false)
	private String passwordHash;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status;
	
	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	
	public static UserEntity create(User user) {
		UserEntity entity = new UserEntity();
		entity.id = user.getId();
		entity.publicId = user.getPublicId().getValue();
		entity.email = user.getEmail().getValue();
		entity.passwordHash = user.getPasswordHash().getValue();
		entity.status = user.getStatus();
		entity.gender = user.getGender();
		entity.role = user.getRole();
		entity.createdAt = user.getCreatedAt();
		entity.updatedAt = user.getUpdatedAt();
		return entity;
	}
	
	public User toDomain() {
		return UserDomainFactory.read(
				id,
				PublicId.of(publicId),
				Email.of(email),
				Nickname.of(nickname),
				PasswordHash.of(passwordHash),
				status,
				gender,
				role,
				createdAt,
				updatedAt
		);
	}
	
	public void changeNickname(Nickname nickname, Instant updatedAt) {
		this.nickname = nickname.getValue();
		changeUpdatedAt(updatedAt);
	}
	
	public void changePasswordHash(PasswordHash passwordHash, Instant updatedAt) {
		this.passwordHash = passwordHash.getValue();
		changeUpdatedAt(updatedAt);
	}
	
	public void changeStatus(UserStatus status, Instant updatedAt) {
		if(this.status == status) return;
		
		this.status = status;
		changeUpdatedAt(updatedAt);
	}
	
	public void changeRole(Role role, Instant updatedAt) {
		if(this.role == role) return;
		
		this.role = role;
		changeUpdatedAt(updatedAt);
	}
	
	public void changeGender(Gender gender, Instant updatedAt) {
		this.gender = gender;
		changeUpdatedAt(updatedAt);
	}
	
	public void changeUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
