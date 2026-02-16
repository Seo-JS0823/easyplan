package com.easyplan._02_application.in;

import com.easyplan.domain.user.model.Gender;
import com.easyplan.domain.user.model.Role;

public final class UserIn {
	public record ChangeRole(String publicId, Role role) {}

	public record Join(String email, String nickname, String password, Gender gender) {}
	
	public record ChangeNickname(String publicId, String newNickname) {}
	
	public record ChangePassword(String email, String oldPassword, String newPassword) {}
	
	public record Login(String email, String password, String refreshToken) {}
}
