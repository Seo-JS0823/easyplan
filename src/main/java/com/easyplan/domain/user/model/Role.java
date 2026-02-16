package com.easyplan.domain.user.model;

/*
 * 회원가입 시 부여되는 권한 = USER
 * 관리자 권한 = ADMIN
 */
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	USER(List.of("ROLE_USER")),
	ADMIN(List.of("ROLE_USER", "ROLE_ADMIN"));
	
	private final List<String> role;
}
