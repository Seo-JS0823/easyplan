package com.easyplan.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.easyplan._02_application.error.UserAppErrorCode;
import com.easyplan._02_application.error.UserAppException;
import com.easyplan._02_application.in.UserIn;
import com.easyplan._02_application.in.UserIn.ChangeNickname;
import com.easyplan._02_application.in.UserIn.ChangePassword;
import com.easyplan._02_application.in.UserIn.Join;
import com.easyplan._02_application.in.UserIn.Login;
import com.easyplan._02_application.out.LoginResult;
import com.easyplan._02_application.service.UserApplicationService;
import com.easyplan.domain.user.model.Gender;

@SpringBootTest
@Transactional
public class UserApplicationTests {
	@Autowired
	private UserApplicationService userService;
	
	private String publicId;
	
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	
	@BeforeEach
	void join() {
		UserIn.Join join = new Join("admin@easyplan.com", "admin", "admin1234@", Gender.NONE);
		String publicId = userService.join(join);
		setPublicId(publicId);
	}
	
	@Test
	void changeNickname() {
		UserIn.ChangeNickname user = new ChangeNickname(publicId, "admin");
		assertThatThrownBy(() -> userService.changeNickname(user))
		.isInstanceOf(UserAppException.class)
		.hasMessage(UserAppErrorCode.CHANGE_SAME_NICKNAME.getMessage());
	}
	
	@Test
	void changePassword() {
		UserIn.ChangePassword user = new ChangePassword("admin@easyplan.com", "admin1234@", "password01@");
		assertThatCode(() -> userService.changePassword(user))
		.doesNotThrowAnyException();
		
		assertThatThrownBy(() -> userService.changePassword(user))
		.isInstanceOf(UserAppException.class)
		.hasMessage(UserAppErrorCode.CHANGE_SAME_PASSWORD.getMessage());
	}
	
	@Test
	void login() {
		UserIn.Login user = new Login("admin@easyplan.com", "admin1234@", null);
		
		LoginResult tokens = userService.login(user);
		System.out.println(tokens.getAccessToken());
		String oneLoginRefreshToken = tokens.getRefreshToken();
		
		UserIn.Login user2 = new Login("admin@easyplan.com", "admin1234@", oneLoginRefreshToken);
		
		LoginResult tokens2 = userService.login(user2);
		System.out.println(tokens2.getAccessToken());
		String twoLoginRefreshToken = tokens2.getRefreshToken();
		
		assertThat(oneLoginRefreshToken.equals(twoLoginRefreshToken)).isFalse();
	}
	
}
