package com.easyplan._04_infra.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.easyplan._03_domain.user.model.User;
import com.easyplan._03_domain.user.model.UserStatus;

import lombok.Getter;

@Getter
public class UserCache {
	private final String publicId;
	
	private final List<SimpleGrantedAuthority> role;
	
	private final boolean deleted;
	
	public UserCache(User user) {
		this.publicId = user.getPublicId().getValue();
		this.role = user.getRole().getRole().stream()
				.map(SimpleGrantedAuthority::new)
				.toList();
		
		if(user.getStatus() == UserStatus.ACTIVE) {
			this.deleted = true;
		} else {
			this.deleted = false;
		}
	}
}
