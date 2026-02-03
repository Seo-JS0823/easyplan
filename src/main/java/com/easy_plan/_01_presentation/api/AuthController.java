package com.easy_plan._01_presentation.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easy_plan._01_presentation.cookie.CookieName;
import com.easy_plan._01_presentation.cookie.CookieProvider;
import com.easy_plan._01_presentation.request.UserRequest;
import com.easy_plan._01_presentation.response.ApiResponse;
import com.easy_plan._01_presentation.response.LoginResponse;
import com.easy_plan._02_application.UserApplication;
import com.easy_plan._02_application.command.UserCommand;
import com.easy_plan._02_application.output.LoginOutput;
import com.easy_plan._02_application.output.SignupOutput;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final UserApplication userApp;
	
	private final CookieProvider cookieProvider;
	
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<?>> signup(@RequestBody UserRequest signup) {
		UserCommand signupCommand = signup.toSignupCommand();
		SignupOutput output = userApp.signup(signupCommand);
		
		Map<String, String> response = Map.of(
			"아이디", output.getEmail(),
			"닉네임", output.getNickname()
		);
		
		return ResponseEntity.ok(ApiResponse.success("회원가입이 정상적으로 처리되었습니다.", response));
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<?>> login(@RequestBody UserRequest login,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		UserCommand loginCommand = login.toLoginCommand();
		String inAccessToken = (String) request.getAttribute("access_token");
		String inRefreshToken = cookieProvider.getCookieValue(CookieName.REFRESH, request);
		
		LoginOutput output = userApp.login(loginCommand, inAccessToken, inRefreshToken);
		
		LoginResponse loginResponse = LoginResponse.builder()
				.accessToken(output.getAccessToken())
				.build();
		
		cookieProvider.addCookie(CookieName.REFRESH, output.getRefreshToken(), response);
		
		return ResponseEntity.ok(ApiResponse.success("정상적으로 로그인 되었습니다.", loginResponse));
	}
	
}
