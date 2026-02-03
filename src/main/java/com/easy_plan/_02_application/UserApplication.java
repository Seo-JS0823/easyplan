package com.easy_plan._02_application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easy_plan._02_application.command.UserCommand;
import com.easy_plan._02_application.exception.TokenBlacklistErrorCode;
import com.easy_plan._02_application.exception.TokenBlacklistException;
import com.easy_plan._02_application.output.LoginOutput;
import com.easy_plan._02_application.output.SignupOutput;
import com.easy_plan._03_domain.auth.AuthService;
import com.easy_plan._03_domain.auth.model.RefreshToken;
import com.easy_plan._03_domain.user.UserService;
import com.easy_plan._03_domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserApplication {
	private final UserService userService;
	
	private final AuthService authService;
	
	@Transactional
	public SignupOutput signup(UserCommand userCommand) {
		User domain = userCommand.toSignupDomain();
		User saved = userService.createUser(domain);
		
		return SignupOutput.builder()
				.email(saved.getEmail().getValue())
				.nickname(saved.getNickname().getValue())
				.gender(saved.getGender().name())
				.createdAt(saved.getCreatedAt())
				.build();
	}
	
	@Transactional
	public LoginOutput login(UserCommand userCommand, String inAccessToken, String inRefreshToken) {
		boolean isInAccessToken = inAccessToken != null && !inAccessToken.isBlank();
		
		// User 조회 및 아이디, 패스워드 검증
		User domain = userCommand.toLoginDomain();
		User loginValid = userService.loginValidate(domain);
		
		String newAccessToken = authService.createAccessToken(loginValid.getId(), loginValid.getRole().name()).getValue();
		RefreshToken newRefreshToken = authService.createRefreshToken(loginValid.getId());
		
		// 요청 액세스 토큰이 존재하는 경우
		if(isInAccessToken) {
			if(!authService.isBlacklistAccessToken(inAccessToken)) {
				authService.addBlacklistAccessToken(inAccessToken);
			} else {
				throw new TokenBlacklistException(TokenBlacklistErrorCode.BLACKLIST_INVALID_ACCESS_TOKEN);
			}
		}
		
		if(inRefreshToken != null) {
			if(!authService.isBlacklistRefreshToken(inRefreshToken)) {
				authService.addBlacklistRefreshToken(loginValid.getId(), inRefreshToken);
			} else {
				throw new TokenBlacklistException(TokenBlacklistErrorCode.BLACKLIST_INVALID_REFRESH_TOKEN);
			}
		}
		
		// 인증 정보 저장 및 업데이트
		Long userId = loginValid.getId();
		authService.updateOrCreateAuth(userId, newRefreshToken);
		
		return LoginOutput.builder()
				.accessToken(newAccessToken)
				.refreshToken(newRefreshToken.getValue())
				.build();
	}
	
	@Transactional
	public void logout(UserCommand userCommand, String inAccessToken, String inRefreshToken) {
		
	}
}
