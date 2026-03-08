package com.easyplan._02_application.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.easyplan._02_application.result.UserResult;
import com.easyplan._02_application.result.UserResult.Profile;
import com.easyplan._03_domain.user.model.PublicId;
import com.easyplan._03_domain.user.model.User;
import com.easyplan._03_domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserApplication {
	private final UserService userService;
	
	@PreAuthorize("hasRole('USER')")
	public UserResult.Profile getProfileInfo(String publicId) {
		User user = userService.loadUserActiveByPublicId(PublicId.of(publicId));
		
		UserResult.Profile result = new Profile(
				user.getPublicId().getValue(),
				user.getEmail().getValue(),
				user.getNickname().getValue(),
				user.getGender(),
				user.getCreatedAt()
		);
		
		return result;
	}
}
