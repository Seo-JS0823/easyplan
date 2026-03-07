package com.easyplan._02_application.service;

import org.springframework.stereotype.Service;

import com.easyplan._02_application.command.UserCommand;
import com.easyplan._02_application.result.UserResult;
import com.easyplan._02_application.result.UserResult.Signup;
import com.easyplan._03_domain.user.model.Email;
import com.easyplan._03_domain.user.model.Nickname;
import com.easyplan._03_domain.user.model.Password;
import com.easyplan._03_domain.user.model.User;
import com.easyplan._03_domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthApplication {
	private final UserService userService;
	
	// 회원가입
	public UserResult.Signup signup(UserCommand.Signup command) {
		Email email = Email.of(command.email());
		Nickname nickname = Nickname.of(command.nickname());
		Password password = Password.of(command.password());
		
		User created = userService.register(email, nickname, password, command.gender());
		
		return new Signup("회원가입이 완료되었습니다.", created.getEmail().getValue());
	}
}
