package com.easyplan._01_web.webutil;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieProvider {
	public void addCookie(CookieName cookie, String value, HttpServletResponse response) {
		response.addHeader(HttpHeaders.SET_COOKIE, generateCookie(cookie, value));
	}
	
	public void clearCookie(CookieName cookie, HttpServletResponse response) {
		response.addHeader(HttpHeaders.SET_COOKIE, generateCookie(cookie, ""));
	}
	
	private String generateCookie(CookieName cookie, String value) {
		ResponseCookie responseCookie = ResponseCookie.from(cookie.getName(), value)
				.path("/")
				.httpOnly(cookie.isHttpOnly())
				.secure(true)
				.maxAge(cookie.getMaxAge())
				.sameSite("strict")
				.build();
		
		return responseCookie.toString();
	}
	
	public String getCookieValue(CookieName cookie, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return null;
		
		for(Cookie c : cookies) {
			if(c.getName().equals(cookie.getName())) {
				return c.getValue();
			}
		}
		
		return null;
	}
}
