package com.easy_plan._05_shared;

import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class UtcClock implements Clock {

	@Override
	public Instant now() {
		return Instant.now();
	}

}
