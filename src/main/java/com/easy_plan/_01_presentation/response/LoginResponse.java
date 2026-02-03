package com.easy_plan._01_presentation.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
	private String accessToken;
}
