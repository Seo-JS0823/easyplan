package com.easy_plan._01_presentation.security.user;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.easy_plan._03_domain.user.model.User;

public class EasyPlanUserDetails implements UserDetails {
	private final UserContext user;
	
	public EasyPlanUserDetails(User user) {
		this.user = new UserContext(user);
	}
	
	public EasyPlanUserDetails(UserContext user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles();
	}

	@Override
	public @Nullable String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	public String getNickname() {
		return user.getNickname();
	}
	
	@Override
	public boolean isEnabled() {
		return !user.isDeleted();
	}
}
