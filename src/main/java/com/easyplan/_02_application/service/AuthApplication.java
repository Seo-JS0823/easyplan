package com.easyplan._02_application.service;

import org.springframework.stereotype.Service;

import com.easyplan._02_application.command.AuthCommand;
import com.easyplan._02_application.command.UserCommand;
import com.easyplan._02_application.result.AuthResult;
import com.easyplan._02_application.result.UserResult;
import com.easyplan._02_application.result.UserResult.Signup;
import com.easyplan._03_domain.auth.model.Auth;
import com.easyplan._03_domain.auth.model.Subject;
import com.easyplan._03_domain.auth.model.TokenPair;
import com.easyplan._03_domain.auth.service.AuthService;
import com.easyplan._03_domain.user.model.Email;
import com.easyplan._03_domain.user.model.Nickname;
import com.easyplan._03_domain.user.model.Password;
import com.easyplan._03_domain.user.model.PublicId;
import com.easyplan._03_domain.user.model.User;
import com.easyplan._03_domain.user.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthApplication {
	private final UserService userService;
	
	private final AuthService authService;
	
	// 회원가입
	@Transactional
	public UserResult.Signup signup(UserCommand.Signup command) {
		Email email = Email.of(command.email());
		Nickname nickname = Nickname.of(command.nickname());
		Password password = Password.of(command.password());
		
		User created = userService.register(email, nickname, password, command.gender());
		
		return new Signup("회원가입이 완료되었습니다.", created.getEmail().getValue());
	}
	
	// 로그인
	@Transactional
	public AuthResult.Login login(UserCommand.Login command) {
		Email email = Email.of(command.email());
		Password password = Password.of(command.password());
		
		User loginUser = userService.login(email, password);
		
		TokenPair tokens = authService.createTokenPair(loginUser.getPublicId().getValue(), loginUser.getRole().name());
		
		authService.loginRegister(
				loginUser.getId(),
				Subject.of(loginUser.getPublicId().getValue()),
				tokens.getRefreshToken()
		);
		
		return new AuthResult.Login(
				"로그인하였습니다.",
				tokens.getAccessToken().getValue(),
				tokens.getRefreshToken().getValue()
		);
	}
	
	// 토큰 재발급
	@Transactional
	public AuthResult.Login reissue(String refreshToken) {
		Auth auth = authService.loadAuthByRefreshToken(refreshToken);
		
		PublicId publicId = PublicId.of(auth.getSubject().getValue());
		
		User user = userService.loadUserActiveByPublicId(publicId);
		
		TokenPair tokens = authService.createTokenPair(publicId.getValue(), user.getRole().name());
		
		authService.rotationRegister(auth, tokens.getRefreshToken());
		
		return new AuthResult.Login(
				publicId.getValue(),
				tokens.getAccessToken().getValue(),
				tokens.getRefreshToken().getValue()
		);
	}
}




















