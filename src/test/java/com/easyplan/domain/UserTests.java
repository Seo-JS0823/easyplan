package com.easyplan.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.easyplan.common.time.Clock;
import com.easyplan.domain.user.error.UserErrorCode;
import com.easyplan.domain.user.error.UserException;
import com.easyplan.domain.user.model.Email;
import com.easyplan.domain.user.model.Gender;
import com.easyplan.domain.user.model.Nickname;
import com.easyplan.domain.user.model.Password;
import com.easyplan.domain.user.model.PasswordHash;
import com.easyplan.domain.user.model.User;
import com.easyplan.domain.user.service.UserService;

@SpringBootTest
@Transactional
class UserTests {

	@Autowired
	private UserService userService;
	
	@Autowired
	private Clock clock;
	
	
	private Email email = Email.of("test@test.com");
	
	private Password password = Password.of("password01@");
	
	private Nickname nickname = Nickname.of("test");
	
	@BeforeEach
	void setUp() {
		userService.register(email, password, nickname, Gender.MALE);
	}
	
	@Test
	@DisplayName("조회 테스트")
	void read() {
		User readUser = userService.readUserByEmail(email);
		
		assertThat(readUser).isNotNull();
		
		assertThatCode(() -> {
			userService.readUserByEmail(Email.of("document@naver.com"));
		})
		.isInstanceOf(UserException.class)
		.hasMessage(UserErrorCode.USER_NOT_FOUND.getMessage());
	}
	
	@Test
	@DisplayName("닉네임 업데이트 테스트")
	void updateNickname() {
		User user = userService.loginValidate(email, password);
		
		Nickname newNickname = Nickname.of("granada");
		user.changeNickname(newNickname, clock.now());
		userService.update(user);
		
		User updated = userService.loginValidate(email, password);
		assertThat(updated.nickname()).isEqualTo(newNickname);
	}
	
	@Test
	@DisplayName("패스워드 업데이트 테스트")
	void updatePassword() {
		User user = userService.loginValidate(email, password);
		
		Password newPassword = Password.of("password02@");
		PasswordHash newPasswordHasher = userService.encodePassword(newPassword);
		
		user.changePassword(newPasswordHasher, clock.now());
		userService.update(user);
		
		User updated = userService.loginValidate(email, newPassword);
		PasswordHash updatedPasswordHasher = updated.passwordHash();
		
		assertThat(newPasswordHasher.equals(updatedPasswordHasher)).isTrue();
	}

	@Test
	@DisplayName("이메일 중복 테스트")
	void duplicateEmailThrow() {
		assertThatCode(() -> {
			userService.duplicateEmailCheck(Email.of("example@test.com"));
		})
		.doesNotThrowAnyException();
		
		assertThatThrownBy(() -> { userService.duplicateEmailCheck(email); })
		.isInstanceOf(UserException.class)
		.hasMessage(UserErrorCode.DUPLICATE_EMAIL.getMessage());
	}
	
	@Test
	@DisplayName("닉네임 중복 테스트")
	void duplicateNicknameThrow() {
		assertThatCode(() -> {
			userService.duplicateNicknameCheck(Nickname.of("easyplan"));
		})
		.doesNotThrowAnyException();
		
		assertThatThrownBy(() -> { userService.duplicateNicknameCheck(nickname); })
		.isInstanceOf(UserException.class)
		.hasMessage(UserErrorCode.DUPLICATE_NICKNAME.getMessage());
	}
	
}
