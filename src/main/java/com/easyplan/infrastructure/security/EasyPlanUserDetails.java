package com.easyplan.infrastructure.security;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.easyplan.domain.user.model.User;

public class EasyPlanUserDetails implements UserDetails {

	private final UserContext user;
	
	public EasyPlanUserDetails(User user) {
		this.user = new UserContext(user);
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

}
