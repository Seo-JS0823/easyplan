package com.easyplan._01_web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyplan._01_web.request.UserRequest;
import com.easyplan._01_web.response.GlobalResponse;
import com.easyplan._02_application.command.UserCommand;
import com.easyplan._02_application.result.UserResult;
import com.easyplan._02_application.service.AuthApplication;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginAPI {
	private final AuthApplication authApp;
	
	@PostMapping("/login")
	public ResponseEntity<GlobalResponse<Object>> login(@RequestBody UserRequest.Login login) {
		UserCommand.Login userCommand = login.toCommand();
		return GlobalResponse.successEntity();
	}
	
	@PostMapping("/signup")
	public ResponseEntity<GlobalResponse<UserResult.Signup>> signup(@RequestBody UserRequest.Signup signup) {
		UserCommand.Signup userCommand = signup.toCommand();
		
		UserResult.Signup result = authApp.signup(userCommand);
		
		return ResponseEntity.ok(GlobalResponse.success(result.message(), result));
	}
}
