package com.easyplan._02_application.out;

import com.easyplan.domain.auth.model.TokenPair;
import com.easyplan.domain.user.model.PublicId;

import lombok.Getter;

@Getter
public class LoginResult {
	private final String publicId;
	
	private final String refreshToken;
	
	private final String accessToken;
	
	public LoginResult(PublicId publicId, TokenPair tokens) {
		this.publicId = publicId.getValue();
		this.refreshToken = tokens.getRefreshToken().getValue();
		this.accessToken = tokens.getAccessToken().getValue();
	}
}
