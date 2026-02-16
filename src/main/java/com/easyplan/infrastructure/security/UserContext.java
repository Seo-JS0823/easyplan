package com.easyplan.infrastructure.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.easyplan.domain.user.model.User;
import com.easyplan.domain.user.model.UserStatus;

import lombok.Getter;

@Getter
public class UserContext {
	private final String publicId;
	
	private final List<SimpleGrantedAuthority> role;
	
	private final boolean deleted;
	
	public UserContext(User user) {
		this.publicId = user.publicId().getValue();
		this.role = user.role().getRole().stream()
				.map(SimpleGrantedAuthority::new)
				.toList();
		
		if(user.status() == UserStatus.ACTIVE) {
			this.deleted = false;
		} else {
			this.deleted = true;
		}
	}

}
