package com.easyplan.domain.auth.provider;

import java.time.Instant;
import java.util.Map;

import com.easyplan.domain.auth.model.AccessToken;
import com.easyplan.domain.auth.model.RefreshToken;
import com.easyplan.domain.auth.model.RefreshTokenHash;
import com.easyplan.domain.auth.model.TokenId;
import com.easyplan.domain.user.model.PublicId;
import com.easyplan.domain.user.model.Role;

public interface TokenProvider {
	/**
	 * AccessToken 생성
	 * 
	 * @param publicId 유저 Public 식별자
	 * @param role 유저 권한
	 * @param now 현재 시간
	 * @return {@link AccessToken}
	 */
	AccessToken createAccessToken(PublicId publicId, Role role, Instant now);
	
	/**
	 * RefreshToken 생성
	 * 
	 * @param now 현재 시간
	 * @return {@link RefreshToken}
	 */
	RefreshToken createRefreshToken(Instant now);
	
	/**
	 * RefreshToken을 SHA-256으로 해시
	 * 
	 * @param refreshToken
	 * @return {@link RefreshTokenHash}
	 */
	RefreshTokenHash hashToken(RefreshToken refreshToken);
	
	/**
	 * 만료된 AccessToken에서 JTI 및 PublicId 추출
	 */
	Map<String, String> expiredExtractUserInfo(String accessToken);
	
	/**
	 * AccessToken에서 JTI 추출
	 * 
	 * @param accessToken
	 * @return {@link TokenId}
	 */
	TokenId extractTokenId(String accessToken);
	
	/**
	 * TokenId 생성 (UUID)
	 * 
	 * @return {@link TokenId}
	 */
	TokenId createTokenId();
	
	/**
	 * AccessToken에서 Subject (PublicId) 추출
	 * 
	 * @param accessToken
	 * @return {@link PublicId}
	 */
	PublicId extractPublicId(String accessToken);
	
	/**
	 * RefreshTokenHash : RefreshToken 비교
	 * 
	 * 
	 */
	boolean validateRefreshToken(RefreshToken rawToken, RefreshTokenHash persistedTokenHash);
}
