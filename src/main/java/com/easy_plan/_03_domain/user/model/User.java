package com.easy_plan._03_domain.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
	private final Email email;
	
	private Password password;
	
	private Nickname nickname;
	
	private Gender gender;
}
