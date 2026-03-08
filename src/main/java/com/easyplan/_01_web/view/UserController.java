package com.easyplan._01_web.view;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.easyplan._01_web.util.CookieName;
import com.easyplan._01_web.util.CookieProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	private final CookieProvider cookie;
	
	@GetMapping("")
	public String unloginView(HttpServletRequest request) {
		String token = cookie.getCookieValue(CookieName.ACCESS, request);
		
		if(token != null) {
			return "redirect:/index";
		}
		
		return "user/unlogin";
	}
	
	@GetMapping("/index")
	@PreAuthorize("hasRole('USER')")
	public String loginView() {
		return "user/login";
	}
	
}
