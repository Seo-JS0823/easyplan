package com.easyplan._01_web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyplan._01_web.request.UserRequest;
import com.easyplan._01_web.response.GlobalResponse;
import com.easyplan._01_web.util.CookieName;
import com.easyplan._01_web.util.CookieProvider;
import com.easyplan._02_application.command.UserCommand;
import com.easyplan._02_application.result.AuthResult;
import com.easyplan._02_application.result.UserResult;
import com.easyplan._02_application.service.AuthApplication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthAPI {
	private final AuthApplication authApp;
	
	private final CookieProvider cookie;
	
	@PostMapping("/login")
	public ResponseEntity<GlobalResponse<Object>> login(
			@RequestBody UserRequest.Login login,
			HttpServletResponse response
	) {
		
		UserCommand.Login userCommand = login.toCommand();
		
		AuthResult.Login result = authApp.login(userCommand);
		
		cookie.addCookie(CookieName.ACCESS, result.accessToken(), response);
		cookie.addCookie(CookieName.REFRESH, result.refreshToken(), response);
		
		return GlobalResponse.successEntity(result.message());
	}
	
	@PostMapping("/signup")
	public ResponseEntity<GlobalResponse<UserResult.Signup>> signup(@RequestBody UserRequest.Signup signup) {
		UserCommand.Signup userCommand = signup.toCommand();
		
		UserResult.Signup result = authApp.signup(userCommand);
		
		return ResponseEntity.ok(GlobalResponse.success(result.message(), result));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<GlobalResponse<Object>> logout(HttpServletRequest request, HttpServletResponse response) {
		cookie.clearCookie(CookieName.CLEAR_ACCESS, response);
		cookie.clearCookie(CookieName.CLEAR_REFRESH, response);
		
		return GlobalResponse.successEntity("로그아웃 완료", null);
	}
}
