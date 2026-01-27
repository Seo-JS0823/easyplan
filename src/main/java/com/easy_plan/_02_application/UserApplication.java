package com.easy_plan._02_application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easy_plan._02_application.command.UserCommand;
import com.easy_plan._03_domain.user.UserService;
import com.easy_plan._03_domain.user.model.User;

import lombok.RequiredArgsConstructor;

/*
 * 
 */
@Service
@RequiredArgsConstructor
public class UserApplication {
	private final UserService userService;
	
	@Transactional
	public void signup(UserCommand userCommand) {
		User signupUser = userCommand.toDomain();
		userService.existsByEmail(signupUser.getEmail());
		userService.existsByNickname(signupUser.getNickname());
		userService.createUser(signupUser);
	}
	
	
}
