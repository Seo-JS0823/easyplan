package com.easyplan._01_web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyplan._01_web.request.UserRequest;
import com.easyplan._01_web.response.GlobalResponse;
import com.easyplan._02_application.result.UserResult;
import com.easyplan._02_application.service.UserApplication;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserAPI {
	private final UserApplication userApp;
	
	@PostMapping("/p-m")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<GlobalResponse<Object>> passwordMatch(
			@RequestBody UserRequest.ProfileUpdate user,
			Authentication auth
	) {
		String publicId = auth.getName();
		
		if(!userApp.passwordMatch(publicId, user.password())) {
			return GlobalResponse.failEntity();
		}
		
		return GlobalResponse.successEntity();
	}
	
	@PatchMapping("/update/p")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<GlobalResponse<Object>> userUpdate(
			@RequestBody UserRequest.ProfilePasswordUpdate user,
			Authentication auth
	) {
		String publicId = auth.getName();
		
		if(!userApp.passwordMatch(publicId, user.currentPassword())) {
			return GlobalResponse.failEntity();
		}
		
		UserResult.Profile profile = userApp.passwordUpdate(publicId, user.newPassword());
		
		return GlobalResponse.successEntity("비밀번호가 변경되었습니다.");
	}
	
}
