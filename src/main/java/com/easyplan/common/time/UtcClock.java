package com.easyplan.common.time;

import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class UtcClock implements Clock {

	@Override
	public Instant now() {
		return Instant.now();
	}

	@Override
	public Instant nowSeconds() {
		return Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);
	}

}
