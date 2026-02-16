package com.easyplan.infrastructure.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.easyplan.common.error.ErrorCode;
import com.easyplan.infrastructure.persistence.auth.provider.JwtTokenErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EasyPlanAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		ErrorCode error = (ErrorCode) request.getAttribute("token-exception");
		String code = error.getCode();
		
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(error.getStatus());
		
		String message;
		
		if(code.equals(JwtTokenErrorCode.EXPIRED.getCode())) {
			message = "EXPIRED";
		} else if(code.equals(JwtTokenErrorCode.MALFORMED.getCode())) {
			message = "WARNING";
		} else if(code.equals(JwtTokenErrorCode.UN_SUPPORTED.getCode())) {
			message = "INVALID";
		} else {
			message = "ERROR";
		}
		
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("message", message);
		errorResponse.put("status", error.getStatus());
		errorResponse.put("code", code);
		
		String result = mapper.writeValueAsString(errorResponse);
		System.out.println("result: " + result);
		response.getWriter().write(result);
	}

}
