package com.easyplan.domain.user.service;

import com.easyplan.common.time.Clock;
import com.easyplan.domain.user.error.UserErrorCode;
import com.easyplan.domain.user.error.UserException;
import com.easyplan.domain.user.model.Email;
import com.easyplan.domain.user.model.Gender;
import com.easyplan.domain.user.model.Nickname;
import com.easyplan.domain.user.model.Password;
import com.easyplan.domain.user.model.PasswordHash;
import com.easyplan.domain.user.model.PublicId;
import com.easyplan.domain.user.model.User;
import com.easyplan.domain.user.model.UserFactory;
import com.easyplan.domain.user.repository.PasswordHasher;
import com.easyplan.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;
	
	private final PasswordHasher passwordHasher;
	
	private final Clock clock;
	
	/**
	 * 새로운 유저를 생성함.
	 * 
	 * @param email    사용할 이메일
	 * @param password 사용할 패스워드(평문)
	 * @param nickname 사용할 닉네임
	 * @param gender   회원가입 유저의 성별
	 * @return 생성된 {@link User} Domain 객체
	 */
	public User register(Email email, Password password, Nickname nickname, Gender gender) {
		PasswordHash encodedPassword = passwordHasher.encode(password);
		
		PublicId publicId = PublicId.create();
		
		User user = UserFactory.create(publicId, email, encodedPassword, nickname, gender, clock.now());
		
		return userRepo.save(user);
	}
	
	/**
	 * 유저의 정보를 업데이트 함.
	 * 
	 * @apiNote 외부에서 new 로 생성한 객체가 아닌 {@link UserService} 로 조회한 객체에
	 * changes Method 를 사용해서 상태가 변경된 객체를 전달하여야 함. 내부에서 한 번더 조회가 일어남.
	 * 
	 * @param user 외부에서 {@link User} 객체가 가진 changes Method로 변경된
	 * User 객체를 파라미터로 줘야함.
	 * @return 변경이 적용된 {@link User} Domain 객체
	 */
	public User update(User user) {
		return userRepo.save(user);
	}
	
	/**
	 * 유저 조회 From AccessToken
	 * 
	 * @throws 존재하지 않는 유저인 경우 UserException 발생
	 * {@link UserErrorCode} USER_NOT_FOUND
	 * 
	 * @param publicId 인증 토큰에서 파싱한 PublicId로 조회함.
	 * @return 조회된 {@link User} Domain 객체
	 */
	public User readUserByPublicId(PublicId publicId) {
		return userRepo.findByPublicId(publicId);
	}
	
	/**
	 * 유저 조회 From Email VO
	 * 
	 * @throws 존재하지 않는 유저인 경우 UserException 발생
	 * {@link UserErrorCode} USER_NOT_FOUND
	 * 
	 * @param email 로그인 등에서 Email 값을 받아서 조회함.
	 * @return 조회된 {@link User} Domain 객체
	 */
	public User readUserByEmail(Email email) {
		return userRepo.findByEmail(email);
	}
	
	/**
	 * 패스워드 해시화 함수
	 * 
	 * @param password {@link Password} 원문을 BCrypt Hash
	 * @return
	 */
	public PasswordHash encodePassword(Password password) {
		return passwordHasher.encode(password);
	}
	
	/**
	 * 패스워드 비교 함수
	 * 
	 * @param raw     비교할 원문 패스워드
	 * @param encoded 비교 대상이 되는 인코딩된 패스워드
	 * @return 두 값이 일치하면 true
	 */
	public boolean passwordMatches(Password raw, PasswordHash encoded) {
		return passwordHasher.matches(raw, encoded);
	}
	
	/**
	 * 로그인 시 사용자 아이디, 패스워드 일치 검증하며
	 * 통과하면 조회한 {@link User} 객체를 반환
	 * 
	 * @param email
	 * @param inputPassword
	 * @return
	 */
	public User loginValidate(Email email, Password inputPassword) {
		User user = userRepo.findByEmail(email);
		
		PasswordHash encodedPassword = user.passwordHash();
		
		boolean match = passwordHasher.matches(inputPassword, encodedPassword);
		if(!match) {
			throw new UserException(UserErrorCode.USER_NOT_FOUND);
		}
		
		return user;
	}
	
	/**
	 * 이메일 중복 체크
	 * 
	 * @throws UserException 이메일 중복시 발생
	 * @param inputEmail
	 */
	public void duplicateEmailCheck(Email inputEmail) {
		Email email = Email.of(inputEmail.getValue());
		
		boolean duplicate = userRepo.existsByEmail(email);
		
		if(duplicate) {
			throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
		}
	}
	
	/**
	 * 닉네임 중복 체크
	 * 
	 * @throws UserException 닉네임 중복시 발생
	 * @param inputNickname
	 */
	public void duplicateNicknameCheck(Nickname inputNickname) {
		Nickname nickname = Nickname.of(inputNickname.getValue());
		
		boolean duplicate = userRepo.existsByNickname(nickname);
		
		if(duplicate) {
			throw new UserException(UserErrorCode.DUPLICATE_NICKNAME);
		}
	}
}
