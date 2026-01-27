package com.easy_plan._05_shared.error;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EasyPlanExceptionHanlder {
	@ExceptionHandler(EasyPlanException.class)
	protected ResponseEntity<?> handleEasyPlanException(EasyPlanException e) {
		EasyPlanErrorCode error = e.getError();
		
		Map<String, Object> errorResponse = Map.of(
			"status", error.getStatus(),
			"error-message", error.getMessage()
		);
		
		return ResponseEntity
				.status(error.getStatus())
				.body(errorResponse);
	}
}
