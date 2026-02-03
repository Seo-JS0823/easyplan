package com.easy_plan._02_application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AuthCommand {
	private final String accessToken;
	
	private final String refreshToken;
}
