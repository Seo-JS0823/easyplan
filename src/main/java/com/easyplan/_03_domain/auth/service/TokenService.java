package com.easyplan._03_domain.auth.service;

import com.easyplan._03_domain.auth.model.AccessToken;
import com.easyplan._03_domain.auth.model.RefreshToken;
import com.easyplan._03_domain.auth.model.RefreshTokenHash;
import com.easyplan._03_domain.auth.model.TokenClaims;

public interface TokenService {
	AccessToken createAccessToken(String publicId, String role);
	
	RefreshToken createRefreshToken();
	
	RefreshTokenHash hashToken(RefreshToken refreshToken);
	
	TokenClaims extractClaims(String accessToken);
	
	boolean validateAccesstoken(String accessToken);
	
	boolean validateRefreshToken(String rawRefreshToken, String encodedRefreshToken);
}
