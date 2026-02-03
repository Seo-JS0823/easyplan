package com.easy_plan._01_presentation.security.user;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.easy_plan._03_domain.user.model.User;

import lombok.Getter;

@Getter
public class UserContext {
	private final String email;
	
	private final String password;
	
	private final String nickname;
	
	private final List<SimpleGrantedAuthority> roles;
	
	private final String gender;
	
	private final boolean deleted;
	
	public UserContext(User user) {
		this.email = user.getEmail().getValue();
		this.password = user.getPassword().getValue();
		this.nickname = user.getNickname().getValue();
		this.gender = user.getGender().name();
		this.deleted = user.isDeleted();
		this.roles = user.getRole().getRoles().stream()
				.map(SimpleGrantedAuthority::new)
				.toList();
	}
}
