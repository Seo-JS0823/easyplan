package com.easyplan.domain.user.repository;

import com.easyplan.domain.user.model.Email;
import com.easyplan.domain.user.model.Nickname;
import com.easyplan.domain.user.model.PasswordHash;
import com.easyplan.domain.user.model.PublicId;
import com.easyplan.domain.user.model.User;

public interface UserRepository {

	/**
	 * 내부적으로 조회 후 저장되게 구현할 것
	 * 
	 * @param user 데이터베이스에 저장할 유저 정보
	 * @return
	 */
	User save(User user);

	/**
	 * 이메일 중복 시 true 반환하게 구현
	 * 
	 * @param email 중복 검사할 이메일
	 * @return 중복: true
	 */
	boolean existsByEmail(Email email);

	/**
	 * 닉네임 중복 시 true 반환하게 구현
	 * 
	 * @param nickname 중복 검사할 닉네임
	 * @return 중복: true
	 */
	boolean existsByNickname(Nickname nickname);

	/**
	 * 사용자의 이메일로 조회 1row
	 * 
	 * @param email 조회할 이메일
	 * @return {@link User}
	 */
	User findByEmail(Email email);

	/**
	 * 사용자의 식별자로 조회 1row
	 * 
	 * @param publicId Token의 Subject인 PublicId로 조회
	 * @return {@link User}
	 */
	User findByPublicId(PublicId publicId);
	
	/**
	 * PublicId로 조회해서 PasswordHash 컬럼 조회
	 * 
	 * @param publicId 조회할 PublicId
	 * @return {@link PasswordHash}
	 */
	PasswordHash getPasswordHash(PublicId publicId);
}
