package com.enfocareservice.config;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		System.err.println("Beginning handshake...");
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
		String authToken = servletRequest.getHeader("Authorization");
		System.err.println("Token found: " + authToken);

		if (authToken == null || !validateToken(authToken)) {
			System.err.println("Invalid or no token found.");
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return false;
		}
		System.err.println("Handshake successful.");
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		System.err.println("After handshake process.");
	}

	private boolean validateToken(String token) {
		// Implement token validation logic here
		System.err.println("Validating token: " + token);
		// Simulated validation logic
		return "valid-token".equals(token);
	}
}
