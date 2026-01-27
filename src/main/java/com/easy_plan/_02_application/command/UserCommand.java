package com.easy_plan._02_application.command;

import com.easy_plan._03_domain.user.model.Email;
import com.easy_plan._03_domain.user.model.Nickname;
import com.easy_plan._03_domain.user.model.Password;
import com.easy_plan._03_domain.user.model.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCommand {
	private final String email;
	
	private final String password;
	
	private final String nickname;
	
	public User toDomain() {
		return User.builder()
				.email(new Email(email))
				.password(new Password(password))
				.nickname(new Nickname(nickname))
				.build();
	}
}
