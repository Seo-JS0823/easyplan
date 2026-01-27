package com.easy_plan.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.easy_plan._03_domain.user.UserService;
import com.easy_plan._03_domain.user.model.Email;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class UserTest {

	@Autowired
	private UserService userService;
	
	@Test
	void userEquals() {
		Email e = new Email(null);
	}
	
}
