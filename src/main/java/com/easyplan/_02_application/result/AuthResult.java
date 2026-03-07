package com.easyplan._02_application.result;

public final class AuthResult {
	public record Login(String message, String accessToken, String refreshToken) {}
}
