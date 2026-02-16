package com.easyplan._01_web.webutil;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CookieName {
	REFRESH("RTID", 60 * 60 * 24 * 7, true),
	CLEAR_REFRESH("RTID", 0, true),
	REMEMBER("RTRBID", 60 * 60 * 24 * 7, true),
	CLEAR_REMEMBER("RTRBID", 0, true),
	REMEMBER_HINT("RBH", 60 * 60 * 24 * 7, false),
	CLEAR_REMEMBER_HINT("RBH", 0, true),
	ACCESS("ATKEY", 60 * 30, true),
	CLEAR_ACCESS("ATKEY", 0, true);
	
	private final String name;
	
	private final int maxAge;
	
	private final boolean httpOnly;
}
