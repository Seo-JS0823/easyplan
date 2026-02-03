package com.easy_plan._03_domain.auth;

import com.easy_plan._03_domain.auth.model.AccessToken;
import com.easy_plan._03_domain.auth.model.Jti;
import com.easy_plan._03_domain.auth.model.RefreshToken;
import com.easy_plan._03_domain.auth.model.TokenClaims;
import com.easy_plan._03_domain.auth.model.TokenHash;

public interface JwtTokenProvider {
	Long getUserIdFromToken(String token);
	
	Jti getJtiFromToken(String token);
	
	AccessToken createAccessToken(Long userId, String role);
	
	RefreshToken createRefreshToken(Long userId);
	
	TokenClaims extractAccessToken(String accessToken);
	
	String validateAccessToken(String accessToken);
	
	boolean validateRefreshToken(String tokenValue, TokenHash tokenHash);
	
	Jti createJti();
}
