package com.easy_plan._03_domain.user.model;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	USER(List.of("ROLE_USER")),
	ADMIN(List.of("ROLE_USER", "ROLE_EASYPLAN"))
	
	;
	private final List<String> roles;
}
