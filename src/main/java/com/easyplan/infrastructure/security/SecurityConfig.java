package com.easyplan.infrastructure.security;

import java.util.List;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.easyplan.domain.auth.provider.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final TokenProvider tokenProv;
	
	private final UserDetailsService userDetailsService;
	
	private final EasyPlanAuthenticationEntryPoint entryPoint;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		CsrfTokenRequestAttributeHandler csrfRequestHandler = new CsrfTokenRequestAttributeHandler();
    csrfRequestHandler.setCsrfRequestAttributeName(null);
		
		http
		.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.formLogin(AbstractHttpConfigurer::disable)
		.httpBasic(AbstractHttpConfigurer::disable)
		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		.csrf(csrf -> csrf
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.csrfTokenRequestHandler(csrfRequestHandler))
		.exceptionHandling(exception -> exception
				.authenticationEntryPoint(entryPoint))
		.addFilterAfter(csrfCookieFilter(), BasicAuthenticationFilter.class)
		.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
		.authorizeHttpRequests(auth -> auth
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				.permitAll()
				
				.requestMatchers("/", "/api/auth/join", "/api/auth/rotation")
				.permitAll()
				
				.anyRequest().authenticated()
		)
		
		;
		return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("https://localhost:8080");
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setExposedHeaders(List.of("Set-Cookie"));
		config.setMaxAge(3600L);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		return source;
	}
	
	@Bean
	JwtFilter jwtFilter() {
		return new JwtFilter(tokenProv, userDetailsService);
	}
	
	@Bean
	CsrfCookieFilter csrfCookieFilter() {
		return new CsrfCookieFilter();
	}
	
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
				.requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/error", "/.well-known/**");
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
