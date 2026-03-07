package com.easyplan._02_application.command;

import com.easyplan._03_domain.user.model.Gender;

public final class UserCommand {
	public record Login(String email, String password) {
		
	}
	
	public record Signup(String email, String password, String nickname, Gender gender) {
		
	}
	
}
