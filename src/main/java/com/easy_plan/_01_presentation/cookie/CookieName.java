package com.easy_plan._01_presentation.cookie;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CookieName {
	REFRESH("RTID", 60 * 60 * 24 * 7, true),
	CLEAR_REFRESH("RTID", 0, true);
	
	private final String name;
	
	private final int maxAge;
	
	private final boolean httpOnly;
}
