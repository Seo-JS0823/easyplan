package com.easyplan.shared.time;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class UtcClock implements Clock {
	
	@Override
	public Instant now() {
		return Instant.now();
	}

	@Override
	public Instant nowSecond() {
		return Instant.now().truncatedTo(ChronoUnit.SECONDS);
	}

	@Override
	public Instant convertToUtcInstant(String dateStr, String clientZoneId, boolean isEndOfDay) {
		try {
			ZoneId zoneId = ZoneId.of(clientZoneId);
			
			LocalDate date = LocalDate.parse(dateStr);
			
			ZonedDateTime zonedDateTime = isEndOfDay
					? date.atTime(LocalTime.MAX).atZone(zoneId)
					: date.atStartOfDay(zoneId);
			
			return zonedDateTime.toInstant();
		} catch (DateTimeException e) {
			return Instant.now();
		}
	}

}
