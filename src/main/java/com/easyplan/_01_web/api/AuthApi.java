package com.easyplan._01_web.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyplan._01_web.request.JoinRequest;
import com.easyplan._01_web.request.LoginRequest;
import com.easyplan._01_web.webutil.CookieName;
import com.easyplan._01_web.webutil.CookieProvider;
import com.easyplan._02_application.in.AuthIn;
import com.easyplan._02_application.in.AuthIn.Rotation;
import com.easyplan._02_application.in.UserIn;
import com.easyplan._02_application.in.UserIn.Join;
import com.easyplan._02_application.in.UserIn.Login;
import com.easyplan._02_application.out.LoginResult;
import com.easyplan._02_application.out.RotationResult;
import com.easyplan._02_application.service.UserApplicationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApi {
	
	private final UserApplicationService userService;
	
	private final CookieProvider cookieProv;
	
	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody JoinRequest join) {
		UserIn.Join joinData = new Join(join.getEmail(), join.getNickname(), join.getPassword(), join.getGender());
		
		String joinPublicId = userService.join(joinData);
		
		return ResponseEntity.ok(Map.of(
			"PublicId", joinPublicId
		));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest login, HttpServletRequest request, HttpServletResponse response) {
		String inputRefreshToken = cookieProv.getCookieValue(CookieName.REFRESH, request);
		
		UserIn.Login loginData = new Login(login.getEmail(), login.getPassword(), inputRefreshToken);
		
		LoginResult loginResult = userService.login(loginData);
		
		String refreshToken = loginResult.getRefreshToken();
		String accessToken = loginResult.getAccessToken();
		String publicId = loginResult.getPublicId();
		
		cookieProv.addCookie(CookieName.ACCESS, accessToken, response);
		cookieProv.addCookie(CookieName.REFRESH, refreshToken, response);
		
		return ResponseEntity.ok(Map.of(
			"RefreshToken", refreshToken,
			"AccessToken", accessToken,
			"PublicId", publicId
		));
	}
	
	@PostMapping("/rotation")
	public ResponseEntity<?> rotation(HttpServletRequest request, HttpServletResponse response) {
		String accessToken = cookieProv.getCookieValue(CookieName.ACCESS, request);
		String refreshToken = cookieProv.getCookieValue(CookieName.REFRESH, request);
		
		AuthIn.Rotation rotationData = new Rotation(accessToken, refreshToken);
		
		RotationResult rotationResult = userService.rotation(rotationData);
		
		String newAT = rotationResult.getNewAccessToken();
		String newRT = rotationResult.getNewRefreshToken();
		
		cookieProv.addCookie(CookieName.ACCESS, newAT, response);
		cookieProv.addCookie(CookieName.REFRESH, newRT, response);
		
		return ResponseEntity.ok(Map.of(
			"Rotation-RefreshToken", newRT,
			"Rotation-AccessToken", newAT
		));
	}
	
}
