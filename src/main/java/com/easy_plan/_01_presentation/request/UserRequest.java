package com.easy_plan._01_presentation.request;

import com.easy_plan._02_application.command.UserCommand;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
	private String email;
	
	private String password;
	
	private String nickname;
	
	private String gender;
	
	public UserCommand toSignupCommand() {
		return UserCommand.builder()
				.email(email)
				.password(password)
				.nickname(nickname)
				.gender(gender)
				.build();
	}
	
	public UserCommand toLoginCommand() {
		return UserCommand.builder()
				.email(email)
				.password(password)
				.build();
	}
	
}
