package com.easyplan.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.easyplan.common.time.Clock;
import com.easyplan.domain.auth.model.AccessToken;
import com.easyplan.domain.auth.provider.TokenProvider;
import com.easyplan.domain.auth.service.AuthSessionService;
import com.easyplan.domain.user.model.PublicId;
import com.easyplan.domain.user.model.Role;

@SpringBootTest
public class AuthTests {
	@Autowired
	private TokenProvider tokenProv;
	
	@Autowired
	private AuthSessionService authService;
	
	@Autowired
	private Clock clock;
	
	@BeforeEach
	void setUp() {
		
	}
	
	@Test
	void createAccessTokenAndValidate() {
		PublicId publicId = PublicId.create();
		Instant now = clock.nowSeconds();
		
		AccessToken accessToken = tokenProv.createAccessToken(publicId, Role.USER, now);
		
		PublicId extractPublicId = tokenProv.extractPublicId(accessToken.getValue());
		
		// 생성한 PublicId와 추출한 PublicId가 같은지 검사
		assertThat(publicId.equals(extractPublicId)).isTrue();
	}
	
	@Test
	void rotation() {
		
	}
}
