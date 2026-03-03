package com.easyplan.shared.time;

import java.time.Instant;

public interface Clock {
	Instant now();
	
	Instant nowSecond();
	
	/**
	 * boolean isEndOfDay true  : 그날의 마지막 시간을 UTC Time으로 반환
	 * boolean isEndOfDay false : 그날의 시작 시간을 UTC Time으로 반환
	 */
	Instant convertToUtcInstant(String date, String clientZoneId, boolean isEndOfDay);
}
