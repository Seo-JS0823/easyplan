package com.easyplan._01_web.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CookieName {
	REFRESH("RTID", 60 * 60 * 24 * 7, true),
	CLEAR_REFRESH("RTID", 0, true),
	
	ACCESS("ATID", 60 * 30, true),
	CLEAR_ACCESS("ATID", 0, true);
	;
	private final String name;
	
	private final int maxAge;
	
	private final boolean httpOnly;
}
