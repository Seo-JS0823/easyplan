package com.easyplan._04_infra.security;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.easyplan._03_domain.user.model.User;

public class CustomUserDetails implements UserDetails {

	private final UserCache user;
	
	public CustomUserDetails(User user) {
		this.user = new UserCache(user);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRole();
	}

	@Override
	public @Nullable String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return user.getPublicId();
	}

	@Override
	public boolean isEnabled() {
		return user.isDeleted();
	}

	
}
