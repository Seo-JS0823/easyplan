package com.easy_plan._04_infra.user;

import java.time.Instant;

import com.easy_plan._03_domain.user.model.Email;
import com.easy_plan._03_domain.user.model.Gender;
import com.easy_plan._03_domain.user.model.Nickname;
import com.easy_plan._03_domain.user.model.Password;
import com.easy_plan._03_domain.user.model.Role;
import com.easy_plan._03_domain.user.model.User;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long id;
	
	@Column(name = "EMAIL", unique = true, nullable = false, length = 200)
	private String email;
	
	@Column(name = "PASSWORD", nullable = false, length = 300)
	private String password;
	
	@Column(name = "NICKNAME", unique = true, nullable = false, length = 10)
	private String nickname;
	
	@Column(name = "GENDER", nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(name = "ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;;
	
	@Column(name = "CREATED_AT", nullable = false, updatable = false)
	private Instant createdAt;
	
	@Column(name = "UPDATED_AT", nullable = false)
	private Instant updatedAt;
	
	public static UserEntity toEntity(User user) {
		return UserEntity.builder()
				.id(user.getId())
				.email(user.getEmail().getValue())
				.password(user.getPassword().getValue())
				.nickname(user.getNickname().getValue())
				.gender(user.getGender())
				.role(user.getRole())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();
	}
	
	public User toDomain() {
		return User.builder()
				.id(id)
				.email(new Email(email))
				.password(new Password(password))
				.nickname(new Nickname(nickname))
				.gender(gender)
				.role(role)
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.build();
	}
}
