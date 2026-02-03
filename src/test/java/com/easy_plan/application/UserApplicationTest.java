package com.easy_plan.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.easy_plan._02_application.UserApplication;
import com.easy_plan._03_domain.auth.JwtTokenProvider;
import com.easy_plan._03_domain.auth.TokenBlacklistRepository;

@SpringBootTest
public class UserApplicationTest {
	@Autowired
	private UserApplication userApp;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private TokenBlacklistRepository blacklistRepo;
	
}
