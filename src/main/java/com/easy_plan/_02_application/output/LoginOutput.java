package com.easy_plan._02_application.output;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginOutput {
	private String accessToken;
	
	private String refreshToken;
}
