package com.easy_plan._02_application.command;

import com.easy_plan._03_domain.user.model.Email;
import com.easy_plan._03_domain.user.model.Gender;
import com.easy_plan._03_domain.user.model.Nickname;
import com.easy_plan._03_domain.user.model.Password;
import com.easy_plan._03_domain.user.model.Role;
import com.easy_plan._03_domain.user.model.User;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserCommand {
	private final String email;
	
	private final String password;
	
	private final String nickname;
	
	private final String gender;
	
	private final String role;
	
	public User toSignupDomain() {
		return User.builder()
				.email(new Email(email))
				.password(new Password(password))
				.nickname(new Nickname(nickname))
				.gender(Gender.valueOf(gender))
				.role(Role.USER)
				.build();
	}
	
	public User toLoginDomain() {
		return User.builder()
				.email(new Email(email))
				.password(new Password(password))
				.build();
	}
}
