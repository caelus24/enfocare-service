package com.enfocareservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.enfocareservice.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserStatusService userStatusService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		jwt = authHeader.substring(7);

		System.err.println(authHeader.substring(7));
		var storedToken = tokenRepository.findByToken(jwt).orElse(null);
		if (storedToken != null) {
			storedToken.setExpired(true);
			storedToken.setRevoked(true);
			tokenRepository.save(storedToken);
			SecurityContextHolder.clearContext();
		}

		String userEmail = jwtService.extractUsername(jwt);

		userStatusService.setUserOffline(userEmail);
	}
}