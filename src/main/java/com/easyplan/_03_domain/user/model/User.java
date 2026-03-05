package com.easyplan._03_domain.user.model;

import java.time.Instant;

import com.easyplan._03_domain.user.exception.UserError;
import com.easyplan._03_domain.user.exception.UserException;
import com.easyplan.shared.util.Require;

import lombok.Getter;

@Getter
public class User {
	private final Long id;
	
	private final PublicId publicId;
	
	private final Email email;
	
	private Nickname nickname;
	
	private PasswordHash passwordHash;
	
	private UserStatus status;
	
	private Gender gender;
	
	private Role role;
	
	private final Instant createdAt;
	
	private Instant updatedAt;
	
	User(
			Long id,
			PublicId publicId,
			Email email,
			Nickname nickname,
			PasswordHash passwordHash,
			UserStatus status,
			Gender gender,
			Role role,
			Instant createdAt,
			Instant updatedAt
	) {
		this.id = id;
		this.publicId = Require.require(publicId, "publicId");
		this.email = Require.require(email, "email");
		this.nickname = Require.require(nickname, "nickname");
		this.passwordHash = Require.require(passwordHash, "passwordHash");
		this.status = Require.require(status, "status");
		this.gender = Require.require(gender, "gender");
		this.role = Require.require(role, "role");
		this.createdAt = Require.require(createdAt, "createdAt");
		this.updatedAt = Require.require(updatedAt, "updatedAt");
	}
	
	// ------------------ //	
	// ----- update ----- //
	// ------------------ //
	
	public void updateNickname(Nickname newNickname, Instant now) {
		validateActive();
		
		if(this.nickname.equals(newNickname)) return;
		
		this.nickname = Require.require(newNickname, "newNickname");
		
		onUpdate(now);
	}
	
	public void updatePasswordHash(PasswordHash newPasswordHash, Instant now) {
		validateActive();
		
		this.passwordHash = Require.require(newPasswordHash, "newPasswordHash");
		
		onUpdate(now);
	}
	
	public void changeRole(Role newRole, Instant now) {
		validateActive();
		
		if(this.role.equals(newRole)) return;
		
		this.role = Require.require(newRole, "newRole");
		
		onUpdate(now);
	}
	
	public void disable(Instant now) {
		if(this.status == UserStatus.DISABLED) return;
		
		this.status = UserStatus.DISABLED;
		
		onUpdate(now);
	}
	
	public void active(Instant now) {
		if(this.status == UserStatus.ACTIVE) return;
		
		this.status = UserStatus.ACTIVE;
		
		onUpdate(now);
	}
	
	// ------------------ //
	// ----- GUARDS ----- //
	// ------------------ //
	
	public void validateActive() {
		if(this.status != UserStatus.ACTIVE) {
			throw new UserException(UserError.USER_DISABLED);
		}
	}
	
	private void onUpdate(Instant now) {
		this.updatedAt = now;
	}
}
