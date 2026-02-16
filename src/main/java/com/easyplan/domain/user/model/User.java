package com.easyplan.domain.user.model;

import java.time.Instant;

import com.easyplan.common.utils.Require;
import com.easyplan.domain.user.error.UserErrorCode;
import com.easyplan.domain.user.error.UserException;

public class User {
	/* User 식별 값: PUBLIC_ID
	 * Unique, Not Null, Length(44)
	 * 
	 * 외부에 보여줄 유저 식별 값
	 */
	private final PublicId publicId;
	
	/* User 이메일: EMAIL
	 * Unique, Not Null, Not Update, Length(300)
	 */
	private final Email email;
	
	/* User 검증값: PASSWORD
	 * BCrypt Hash 문자열
	 * Not Null, Length(300)
	 */
	private PasswordHash passwordHash;
	
	/* User 닉네임: NICKNAME
	 * Unique, Not Null, Length(10)
	 */
	private Nickname nickname;
	
	/* User 성별: GENDER
	 * Not Null
	 * 
	 * MALE = 남성, FEMALE = 여성, NONE = 비공개
	 */
	private Gender gender;
	
	/* User 권한: ROLE
	 * Not Null
	 */
	private Role role;
	
	/* User 상태: STATUS
	 * Not Null, Default (ACTIVE), LENGTH(8)
	 * 
	 * ACTIVE = 활성, DISABLED = 비활성
	 */
	private UserStatus status;
	
	/* User 생성일: CREATED_AT
	 * Not Null, Default (NOW), Not Update
	 * 
	 * UTC TIME
	 */
	private final Instant createdAt;
	
	/* User 최신 수정일: UPDATED_AT
	 * Not Null
	 * 
	 * UTC TIME
	 */
	private Instant updatedAt;
	
	// ======================= //
	// ===== Constructor ===== //
	// ======================= //
	
	// package-private UserFactory
	User(
			PublicId publicId,
			Email email,
			Gender gender,
			Nickname nickname,
			PasswordHash passwordHash,
			Role role,
			UserStatus status,
			Instant createdAt,
			Instant updatedAt
	) {
		this.publicId = Require.require(publicId, "publicId");
    this.email = Require.require(email, "email");
    this.gender = Require.require(gender, "gender");
    this.nickname = Require.require(nickname, "nickname");
    this.passwordHash = Require.require(passwordHash, "passwordHash");
    this.role = Require.require(role, "role");
    this.status = Require.require(status, "status");
    this.createdAt = Require.require(createdAt, "createdAt");
    this.updatedAt = Require.require(updatedAt, "updatedAt");
	}
	
	// ============================ //
	// ===== Domain Behaviors ===== //
	// ============================ //

	// 닉네임 변경
	public void changeNickname(Nickname newNickname, Instant now) {
		assertActive();
		
		if(this.nickname.equals(newNickname)) {
			return;
		}
		
		this.nickname = Require.require(newNickname, "newNickname");
		onUpdate(now);
	}
	
	// 패스워드 변경
	public void changePassword(PasswordHash newPasswordHash, Instant now) {
		assertActive();
		this.passwordHash = Require.require(newPasswordHash, "newPasswordHash");
		onUpdate(now);
	}
	
	// 권한 변경
	public void changeRole(Role newRole, Instant now) {
		assertActive();
		this.role = Require.require(newRole, "newRole");
		onUpdate(now);
	}
	
	// 탈퇴 처리
	public void disable(Instant now) {
		if(this.status == UserStatus.DISABLED) return;
		this.status = UserStatus.DISABLED;
		onUpdate(now);
	}
	
	// 탈퇴 처리 복구
	public void active(Instant now) {
		if(this.status == UserStatus.ACTIVE) return;
		this.status = UserStatus.ACTIVE;
		onUpdate(now);
	}
	
	// ========================= //
	// ===== Domain Guards ===== //
	// ========================= //
	
	// 도메인 상태 변경 전 변경이 가능한 상태인지 확인
	private void assertActive() {
		if(this.status != UserStatus.ACTIVE) {
			throw new UserException(UserErrorCode.USER_DISABLED);
		}
	}
	
	// 도메인 상태 변경 후 최신 수정일을 업데이트
	private void onUpdate(Instant now) {
    this.updatedAt = now;
	}
	
	public PublicId publicId() { return publicId; }
	public Email email() { return email; }
	public PasswordHash passwordHash() { return passwordHash; }
	public Gender gender() { return gender; }
	public Nickname nickname() { return nickname; }
	public Role role() { return role; }
	public UserStatus status() { return status; }
	public Instant createdAt() { return createdAt; }
	public Instant updatedAt() { return updatedAt; }
	
	@Override
	public String toString() {
		String publicId = this.publicId.getValue();
		String email = this.email.getValue();
		String nickname = this.nickname.getValue();
		String gender = this.gender.name();
		String role = this.role.name();
		String status = this.status.name();
		
		return String.format(
				"[User = PublicId: %s, Email: %s, Nickname: %s, Gender: %s, Role: %s, Status: %s]",
				publicId, email, nickname, gender, role, status
		);
	}
}
