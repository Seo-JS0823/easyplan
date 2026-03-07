package com.easyplan._03_domain.auth.model;

import com.easyplan._03_domain.auth.exception.AuthException;
import com.easyplan._03_domain.auth.exception.AuthValueError;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TokenPair {
	private final AccessToken accessToken;
	
	private final RefreshToken refreshToken;
	
	public TokenPair(AccessToken accessToken, RefreshToken refreshToken) {
		if(accessToken == null || refreshToken == null) {
			throw new AuthException(AuthValueError.INVALID_TOKEN_PAIR);
		}
		
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
