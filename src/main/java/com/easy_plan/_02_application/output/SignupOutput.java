package com.easy_plan._02_application.output;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupOutput {
	private String email;
	
	private String nickname;
	
	private String gender;
	
	private Instant createdAt;
}
