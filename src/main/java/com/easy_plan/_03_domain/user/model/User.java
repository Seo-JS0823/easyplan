package com.easy_plan._03_domain.user.model;

import java.time.Instant;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
	private final Long id;
	
	private final Email email;
	
	private Password password;
	
	private Nickname nickname;
	
	private Gender gender;
	
	private Role role;
	
	private Instant createdAt;
	
	private Instant updatedAt;
	
	private boolean deleted;
	
	public void encodePassword(String encodedPassword) {
		this.password = new Password(encodedPassword);
	}
	
	public void passwordUpdate(Password newPassword) {
		this.password = newPassword;
	}
	
	public void createdAt(Instant now) {
		this.createdAt = now;
	}
	
	public void updatedAt(Instant now) {
		this.updatedAt = now;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		User other = (User) obj;
		return id != null && Objects.equals(id, other.id);
	}
}
