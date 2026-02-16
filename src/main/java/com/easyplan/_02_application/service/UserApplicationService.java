package com.easyplan._02_application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyplan._02_application.error.UserAppErrorCode;
import com.easyplan._02_application.error.UserAppException;
import com.easyplan._02_application.in.AuthIn;
import com.easyplan._02_application.in.UserIn;
import com.easyplan._02_application.out.LoginResult;
import com.easyplan._02_application.out.RotationResult;
import com.easyplan.common.time.Clock;
import com.easyplan.domain.auth.model.AuthSession;
import com.easyplan.domain.auth.model.RefreshTokenHash;
import com.easyplan.domain.auth.model.Subject;
import com.easyplan.domain.auth.model.TokenPair;
import com.easyplan.domain.auth.service.AuthSessionService;
import com.easyplan.domain.user.model.Email;
import com.easyplan.domain.user.model.Nickname;
import com.easyplan.domain.user.model.Password;
import com.easyplan.domain.user.model.PasswordHash;
import com.easyplan.domain.user.model.PublicId;
import com.easyplan.domain.user.model.User;
import com.easyplan.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
	private final UserService userService;
	
	private final AuthSessionService authService;
	
	private final Clock clock;
	
	/**
	 * 
	 */
	@Transactional
	public RotationResult rotation(AuthIn.Rotation rotation) {
		String accessToken = rotation.accessToken();
		String refreshToken = rotation.refreshToken();
		TokenPair newTokens = authService.rotation(accessToken, refreshToken, clock.now());
		
		return new RotationResult(newTokens);
	}
	
	
	/**
	 * 로그인 Usecase
	 * <br><br>
	 * 1. 로그인 아이디, 패스워드 검증
	 * 2. 리프레시 토큰이 있을 경우 검증 및 블랙리스트 추가
	 * 3. 새로운 토큰세트 생성 및 인증 정보 DB 업데이트
	 * 
	 * @param login
	 * @return
	 */
	@Transactional
	public LoginResult login(UserIn.Login login) {
		Email email = Email.of(login.email());
		Password password = Password.of(login.password());
		String inputRefreshToken = login.refreshToken();
		Instant now = clock.now();
		
		User loginUser = userService.loginValidate(email, password);
		
		AuthSession authSession = authService.loginRefreshToken(loginUser, inputRefreshToken, now);
		
		TokenPair tokens = authService.createTokenPair(loginUser, now);
		
		RefreshTokenHash newRefreshTokenHash = authService.hashToken(tokens.getRefreshToken());
		Instant newRefreshTokenExpiresAt = tokens.getRefreshToken().getExpiresAt();
		
		if(authSession != null) {
			authSession.changeRefreshTokenHash(newRefreshTokenHash, newRefreshTokenExpiresAt, now);
			authService.register(authSession);
		} else {
			authSession = authService.register(Subject.of(loginUser.publicId()), newRefreshTokenHash, newRefreshTokenExpiresAt, now);			
		}
		
		return new LoginResult(loginUser.publicId(), tokens);
	}
	
	/**
	 * 회원가입 Usecase
	 * <br><br>
	 * 이메일 중복 체크 -> 닉네임 중복 체크 -> 회원 등록
	 * @param join 회원가입용 정보(email, password, nickname, gender)
	 */
	@Transactional
	public String join(UserIn.Join join) {
		Email email = Email.of(join.email());
		Nickname nickname = Nickname.of(join.nickname());
		Password password = Password.of(join.password());
		
		userService.duplicateEmailCheck(email);
		userService.duplicateNicknameCheck(nickname);
		
		User saved = userService.register(email, password, nickname, join.gender());
		return saved.publicId().getValue();
	}
	
	/**
	 * 닉네임 변경 Usecase
	 * <br><br>
	 * 닉네임 값 검증 -> 닉네임 중복 체크 -> 대상 조회 (PublicId) -> 업데이트
	 * 
	 * @param
	 */
	@Transactional
	public String changeNickname(UserIn.ChangeNickname user) {
		Nickname newNickname = Nickname.of(user.newNickname());
		
		PublicId publicId = PublicId.of(user.publicId());
		
		User current = userService.readUserByPublicId(publicId);
		
		if(current.nickname().equals(newNickname)) {
			throw new UserAppException(UserAppErrorCode.CHANGE_SAME_NICKNAME);
		}
		
		userService.duplicateNicknameCheck(newNickname);
		
		current.changeNickname(newNickname, clock.now());
		
		User updated = userService.update(current);
		
		return updated.nickname().getValue();
	}
	
	
	@Transactional
	public void changePassword(UserIn.ChangePassword user) {
		User current = userService.readUserByEmail(Email.of(user.email()));
		
		Password newPassword = Password.of(user.newPassword());
		System.out.println(newPassword.getValue());
		
		PasswordHash currentPasswordHash = current.passwordHash();
		System.out.println(currentPasswordHash.getHashedValue());
		
		if(userService.passwordMatches(newPassword, currentPasswordHash)) {
			throw new UserAppException(UserAppErrorCode.CHANGE_SAME_PASSWORD);
		}
		
		PasswordHash newPasswordHash = userService.encodePassword(newPassword);
		
		current.changePassword(newPasswordHash, clock.now());
		
		userService.update(current);
	}
	
	@Transactional
	public void changeRole(UserIn.ChangeRole user) {
		PublicId publicId = PublicId.of(user.publicId());
		
		User current = userService.readUserByPublicId(publicId);
		
		current.changeRole(user.role(), clock.now());
		
		userService.update(current);
	}
}
