package com.easyplan.common.time;

import java.time.Instant;

public interface Clock {
	Instant now();
	
	Instant nowSeconds();
}
