package com.easy_plan._01_presentation.handler;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.easy_plan._03_domain.EasyPlanErrorCode;
import com.easy_plan._03_domain.EasyPlanException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/*
 * 비즈니스 로직 예외 핸들러
 * 
 * 인증/인가 관련 정보는 숨김 처리 후 사용자에게 에러 메시지 응답.
 * 로그로 찍을 땐 전부 다 보임
 */
@RestControllerAdvice
@Slf4j
public class EasyPlanExceptionHanlder {
	
	@ExceptionHandler(EasyPlanException.class)
	protected ResponseEntity<?> handleEasyPlanException(EasyPlanException e, HttpServletRequest request, HttpServletResponse response) {
		EasyPlanErrorCode error = e.getError();
		
		String originalMessage = error.getMessage();
		
		log.error("Handler Exception, ErrorCode: [{}]: {}", error.getClass().getSimpleName(), originalMessage);
		
		String errorMessage = cleanMessage(originalMessage);
		
		Map<String, Object> errorResponse = Map.of(
			"status", error.getStatus(),
			"error-message", errorMessage
		);
		
		return ResponseEntity
				.status(error.getStatus())
				.body(errorResponse);
	}
	
	private String cleanMessage(String message) {
		if(message != null && message.contains("Security:")) {
			return "인증 오류";
		}
		return message;
	}
}
