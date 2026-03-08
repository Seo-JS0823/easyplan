package com.easyplan._02_application.result;

import java.time.Instant;

import com.easyplan._03_domain.user.model.Gender;

public final class UserResult {
	public record Signup(String message, String email) {}
	
	public record Profile(String publicId, String email, String nickname, Gender gender, Instant createdAt) {
		public String getGender() {
			if(this.gender.equals(Gender.MALE)) {
				return "남자";
			} else if(this.gender.equals(Gender.FEMALE)) {
				return "여자";
			} else {
				return "비공개";
			}
		}
	}
}
