package com.easy_plan._01_presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.easy_plan._03_domain.user.model.Email;

@Controller
public class TestController {

	@GetMapping("/")
	public String test() {
		Email e = new Email(null);
		return "test";
	}
	
}
