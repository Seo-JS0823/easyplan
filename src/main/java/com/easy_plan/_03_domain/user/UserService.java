package com.easy_plan._03_domain.user;

import com.easy_plan._03_domain.user.exception.UserErrorCode;
import com.easy_plan._03_domain.user.exception.UserException;
import com.easy_plan._03_domain.user.model.Email;
import com.easy_plan._03_domain.user.model.Nickname;
import com.easy_plan._03_domain.user.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;
	
	public void existsByEmail(Email email) {
		if(userRepo.existsByEmail(email))
			throw new UserException(UserErrorCode.ALREADY_EMAIL);
	}
	
	public void existsByNickname(Nickname nickname) {
		if(userRepo.existsByNickname(nickname))
			throw new UserException(UserErrorCode.ALREADY_NICKNAME);
	}
	
	public User createUser(User user) {
		return userRepo.save(user);
	}
	
	public User findByEmail(Email email) {
		return userRepo.findByEmail(email)
				.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
	}
	
	public User findByNickname(Nickname nickname) {
		return userRepo.findByNickname(nickname)
				.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
	}
	
	public User findByUserId(Long userId) {
		return userRepo.findByUserId(userId)
				.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
	}
	
	public void loginValidate(User user) {
		if(!userRepo.passwordMatch(user))
			throw new UserException(UserErrorCode.LOGIN_NOT_MATCH);
	}
}
