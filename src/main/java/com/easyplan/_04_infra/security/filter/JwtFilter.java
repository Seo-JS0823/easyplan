package com.easyplan._04_infra.security.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.easyplan._01_web.util.CookieName;
import com.easyplan._01_web.util.CookieProvider;
import com.easyplan._02_application.result.AuthResult;
import com.easyplan._02_application.service.AuthApplication;
import com.easyplan._03_domain.auth.service.TokenService;
import com.easyplan._03_domain.user.model.PublicId;
import com.easyplan._03_domain.user.model.User;
import com.easyplan._03_domain.user.repository.UserRepository;
import com.easyplan._04_infra.security.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final TokenService tokenService;
	
	private final UserRepository userRepo;
	
	private final AuthApplication authApp;
	
	private final CookieProvider cookie;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = getToken(CookieName.ACCESS, request);
		String rtToken = getToken(CookieName.REFRESH, request);
		if(token == null) {
			// reissue
			if(rtToken != null) {
				AuthResult.Login reissue = authApp.reissue(rtToken);
				
				cookie.addCookie(CookieName.ACCESS, reissue.accessToken(), response);
				cookie.addCookie(CookieName.REFRESH, reissue.refreshToken(), response);
				
				UsernamePasswordAuthenticationToken authentication = authentication(reissue.message());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				filterChain.doFilter(request, response);
				return;
			}
			
			String requestURI = request.getRequestURI();
			
			if(isRedirectTarget(requestURI)) {
				response.sendRedirect("/");
				return;
			}
			
			filterChain.doFilter(request, response);
			return;
		}
		
		String publicId = null;
		try {
			publicId = tokenService.extractClaims(token).getSubject().getValue();
			
			System.out.println("JWT-Filter: public_id = " + publicId);
			
			UsernamePasswordAuthenticationToken authentication = authentication(publicId);
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch (com.easyplan.shared.exception.GlobalException e) {
			request.setAttribute("token-exception", e.getError());
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken authentication(String publicId) {
		User user = userRepo.findByPublicId(PublicId.of(publicId))
				.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자"));
		
		UserDetails details = new CustomUserDetails(user);
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
		
		return auth;
	}

	private String getToken(CookieName cookie, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return null;
		
		for(Cookie c : cookies) {
			if(c.getName().equals(cookie.getName())) {
				return c.getValue();
			}
		}
		return null;
	}
	
	private boolean isRedirectTarget(String uri) {
    return uri.equals("/index");
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
    AntPathMatcher pathMatcher = new AntPathMatcher();
		
		String[] excludePath = {
			"/css/**", "/js/**", "/img/**", "/favicon.ico", "/error", "/.well-known/**",
			"/", "/api/auth/signup", "/api/auth/login"
    };
		
		return Arrays.stream(excludePath)
        .anyMatch(pattern -> pathMatcher.match(pattern, path));
	}
}
