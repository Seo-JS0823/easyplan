package com.easy_plan._01_presentation.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.easy_plan._01_presentation.security.TokenStatus;
import com.easy_plan._01_presentation.security.user.EasyPlanUserDetails;
import com.easy_plan._01_presentation.security.user.UserContext;
import com.easy_plan._03_domain.auth.JwtTokenProvider;
import com.easy_plan._03_domain.user.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	private static final String ACCESS_TOKEN_ATTRIBUTE = "access_token";
	
	private static final String TOKEN_PREFIX = "Authorization";
	
	private static final String START_PREFIX = "Bearer ";
	
	private static final String TOKEN_INVALID_HEADER = "WWW-Authenticate";
	
	private final JwtTokenProvider tokenProvider;
	
	private final UserRepository userRepo;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = tokenFromRequest(request);
			if(token == null || token.isBlank()) {
				filterChain.doFilter(request, response);
				return;
			}
			
			if(!tokenValidate(token, response)) {
				response.setStatus(401);
				return;
			}
			
			UsernamePasswordAuthenticationToken auth = authentication(token);
			auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			request.setAttribute(ACCESS_TOKEN_ATTRIBUTE, token);
			filterChain.doFilter(request, response);
		} finally {
			SecurityContextHolder.clearContext();
		}
	}
	
	private UsernamePasswordAuthenticationToken authentication(String token) throws UsernameNotFoundException {
		Long userId = tokenProvider.getUserIdFromToken(token);
		
		UserContext user = new UserContext(userRepo.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")));
		
		UserDetails details = new EasyPlanUserDetails(user);
		
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
		
		return authentication;
	}
	
	private String tokenFromRequest(HttpServletRequest request) {
		String tokenFromHeader = request.getHeader(TOKEN_PREFIX);
		
		if(tokenFromHeader == null) {
			return null;
		}
		
		if(tokenFromHeader.startsWith(START_PREFIX) && tokenFromHeader.length() > 7) {
			return tokenFromHeader.substring(7);
		}
		
		return null;
	}
	
	private boolean tokenValidate(String token, HttpServletResponse response) {
		TokenStatus status = TokenStatus.valueOf(tokenProvider.validateAccessToken(token));
		
		String bearer = "Bearer error=\"invalid_token\"";
		if(status == TokenStatus.EXPIRED) {
			String description = ", error_description=\"token_expired\"";
			response.setHeader(TOKEN_INVALID_HEADER, bearer + description);
			return false;
		}
		
		if(status == TokenStatus.INVALID) {
			response.setHeader(TOKEN_INVALID_HEADER, bearer);
			return false;
		}
		
		return true;
	}
}
