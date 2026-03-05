package com.easyplan._03_domain.user.service;

import com.easyplan._03_domain.user.repository.PasswordRepository;
import com.easyplan._03_domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;
	
	private final PasswordRepository passwordRepo;
	
}
