package com.easyplan.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class TokenPair {
	private final AccessToken accessToken;
	
	private final RefreshToken refreshToken;
}
