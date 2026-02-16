package com.easyplan.infrastructure.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.easyplan._01_web.webutil.CookieName;
import com.easyplan.common.error.GlobalException;
import com.easyplan.domain.auth.provider.TokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProv;
	
	private final UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = getToken(request);
		if(token == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String publicId = null;
		try {
			publicId = tokenProv.extractPublicId(token).getValue();
			
			UsernamePasswordAuthenticationToken authentication = authentication(publicId);
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch (GlobalException e) {
			request.setAttribute("token-exception", e.getErrorCode());
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken authentication(String publicId) throws UsernameNotFoundException {
		UserDetails details = userDetailsService.loadUserByUsername(publicId);
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
		
		return auth;
	}

	private String getToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return null;
		
		for(Cookie c : cookies) {
			if(c.getName().equals(CookieName.ACCESS.getName())) {
				return c.getValue();
			}
		}
		return null;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
    AntPathMatcher pathMatcher = new AntPathMatcher();
		
		String[] excludePath = {
			"/"
    };
		
		return Arrays.stream(excludePath)
        .anyMatch(pattern -> pathMatcher.match(pattern, path));
	}
	
}
