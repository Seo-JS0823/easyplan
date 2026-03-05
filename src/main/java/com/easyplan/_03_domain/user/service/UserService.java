package com.easyplan._03_domain.user.service;

import com.easyplan._03_domain.user.exception.UserError;
import com.easyplan._03_domain.user.exception.UserException;
import com.easyplan._03_domain.user.model.Email;
import com.easyplan._03_domain.user.model.Gender;
import com.easyplan._03_domain.user.model.Nickname;
import com.easyplan._03_domain.user.model.Password;
import com.easyplan._03_domain.user.model.PasswordHash;
import com.easyplan._03_domain.user.model.PublicId;
import com.easyplan._03_domain.user.model.User;
import com.easyplan._03_domain.user.model.UserDomainFactory;
import com.easyplan._03_domain.user.repository.UserRepository;
import com.easyplan.shared.time.Clock;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;
	
	private final PasswordService passwordService;
	
	private final Clock clock;
	
	/**
	 * 사용자 생성 (회원가입)
	 * 
	 * 이미 검증된 이메일, 닉네임, 패스워드가 파라미터로 들어옴
	 * 
	 * 중복 검사 후 패스워드는 BCrypt 해시후 저장하게 됨
	 */
	public User register(Email email, Nickname nickname, Password password, Gender gender) {
		if(userRepo.existsByEmail(email)) {
			throw new UserException(UserError.DUPLICATE_EMAIL);
		}
		
		if(userRepo.existsByNickname(nickname)) {
			throw new UserException(UserError.DUPLICATE_NICKNAME);
		}
		
		PasswordHash passwordHash = passwordService.encode(password);
		
		User user = UserDomainFactory.create(email, nickname, passwordHash, gender, clock.now());
		
		return userRepo.save(user);
	}
	
	/**
	 * 사용자 로그인 (로그인)
	 * 
	 * 해당 이메일의 사용자가 존재하지 않거나
	 * 이메일과 패스워드가 일치하지 않으면 같은 LOGIN_NOT_MATCHES 예외를 발생
	 * 
	 * Disabled 계정은 로그인 할 수 없음
	 */
	public User login(Email email, Password password) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new UserException(UserError.LOGIN_NOT_MATCHES));
		
		if(!passwordService.matches(password, user.getPasswordHash())) {
			throw new UserException(UserError.LOGIN_NOT_MATCHES);
		}
		
		user.validateActive();
		
		return user;
	}
	
	/**
	 * PublicId 로 Active 상태의 사용자 조회
	 */
	public User loadUserActiveByPublicId(PublicId publicId) {
		User user = userRepo.findByPublicId(publicId)
				.orElseThrow(() -> new UserException(UserError.USER_NOT_FOUND));
		
		user.validateActive();
		
		return user;
	}
	
	/**
	 * Email 로 Active 상태의 사용자 조회
	 */
	public User loadUserActiveByEmail(Email email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new UserException(UserError.USER_NOT_FOUND));
		
		user.validateActive();
		
		return user;
	}
	
	/**
	 * Id 로 Active 상태의 사용자 조회
	 */
	public User loadUserActiveById(Long id) {
		User user = userRepo.findById(id)
				.orElseThrow(() -> new UserException(UserError.USER_NOT_FOUND));
		
		user.validateActive();
		
		return user;
	}
	
	/**
	 * 닉네임 변경
	 * 
	 * PublicId 조회를 통해 닉네임을 변경하며, 비활성화 상태를 User 내부에서 검증함
	 */
	public void updateNickname(PublicId publicId, Nickname newNickname) {
		if(userRepo.existsByNickname(newNickname)) {
			throw new UserException(UserError.DUPLICATE_NICKNAME);
		}
		
		User user = loadUserActiveByPublicId(publicId);
		
		user.updateNickname(newNickname, clock.now());
		
		userRepo.updateNickname(user);
	}
}
