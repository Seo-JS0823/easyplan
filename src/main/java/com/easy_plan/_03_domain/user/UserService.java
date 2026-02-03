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
	
	// 이메일 중복 확인
	// 중복 -> Exception
	public void existsByEmail(Email email) {
		if(userRepo.existsByEmail(email))
			throw new UserException(UserErrorCode.ALREADY_EMAIL);
	}
	
	// 닉네임 중복 확인
	// 중복 -> Exception
	public void existsByNickname(Nickname nickname) {
		if(userRepo.existsByNickname(nickname))
			throw new UserException(UserErrorCode.ALREADY_NICKNAME);
	}
	
	// 사용자 생성
	// 생성 실패 -> Exception
	public User createUser(User user) {
		return userRepo.save(user);
	}
	
	// 이메일로 사용자 조회
	public User findByEmail(Email email) {
		return userRepo.findByEmail(email)
				.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
	}
	
	// 닉네임으로 사용자 조회
	public User findByNickname(Nickname nickname) {
		return userRepo.findByNickname(nickname)
				.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
	}
	
	// 식별키로 사용자 조회
	public User findByUserId(Long userId) {
		return userRepo.findByUserId(userId)
				.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
	}
	
	// 로그인시 아이디, 패스워드 검증
	public User loginValidate(User user) {
		return userRepo.passwordMatch(user);
	}
}
