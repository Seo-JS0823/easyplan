package com.easyplan._02_application.command;

public class AuthCommand {
	public record Reissue(String refreshToken) {}
}
