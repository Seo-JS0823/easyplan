package com.easyplan._02_application.out;

import com.easyplan.domain.auth.model.TokenPair;

import lombok.Getter;

@Getter
public class RotationResult {
	private final String newAccessToken;
	
	private final String newRefreshToken;
	
	public RotationResult(TokenPair newTokens) {
		this.newAccessToken = newTokens.getAccessToken().getValue();
		this.newRefreshToken = newTokens.getRefreshToken().getValue();
	}
}
