package com.easyplan._01_web.view;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easyplan._02_application.result.UserResult;
import com.easyplan._02_application.service.UserApplication;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my")
public class MypageController {
	private final UserApplication userApp;
	
	@GetMapping("/profile")
	@PreAuthorize("hasRole('USER')")
	public String profileView(Authentication auth, Model model) {
		System.out.println(auth.getName());
		
		String publicId = auth.getName();
		
		UserResult.Profile profile = userApp.getProfileInfo(publicId);
		
		model.addAttribute("user", profile);
		
		return "user/mypage/profile";
	}
}
