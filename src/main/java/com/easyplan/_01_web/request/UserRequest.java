package com.easyplan._01_web.request;

import com.easyplan._02_application.command.UserCommand;
import com.easyplan._03_domain.user.model.Gender;

public final class UserRequest {
	public record Login(String email, String password) {
		public UserCommand.Login toCommand() {
			return new UserCommand.Login(email, password);
		}
	}
	
	public record Signup(String email, String password, String nickname, Gender gender) {
		public UserCommand.Signup toCommand() {
			return new UserCommand.Signup(email, password, nickname, gender);
		}
	}
	
	public record ProfileUpdate(String password) {}
	
	public record ProfilePasswordUpdate(String currentPassword, String newPassword) {}
}
