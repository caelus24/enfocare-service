package com.enfocareservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enfocareservice.model.AuthenticationRequest;
import com.enfocareservice.model.AuthenticationResponse;
import com.enfocareservice.model.CheckTokenRequest;
import com.enfocareservice.model.RegisterRequest;
import com.enfocareservice.service.AuthenticationService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
		try {
			LOGGER.info("AuthenticationController : register started {}", registerRequest);
			LOGGER.info("Register request received TANGINA: {}", registerRequest);
			AuthenticationResponse response = authenticationService.register(registerRequest);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOGGER.error("Registration  TANGINA failed for {}", registerRequest, e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(
			@RequestBody AuthenticationRequest authenticationRequest) {

		AuthenticationResponse authenticationResponse = new AuthenticationResponse();

		authenticationResponse = authenticationService.authenticate(authenticationRequest);

		authenticationResponse.setResponseCode(200);
		return ResponseEntity.ok(authenticationResponse);

	}

	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			authenticationService.refreshToken(request, response);
		} catch (StreamWriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PostMapping("/check-token")
	public ResponseEntity<String> checkToken(@RequestBody CheckTokenRequest checkTokenRequest) {

		System.err.println(" checkToken CALLED" + checkTokenRequest.getToken());
		try {
			if (authenticationService.checkTokenValidity(checkTokenRequest.getToken(), checkTokenRequest.getEmail())) {
				return ResponseEntity.ok("valid");
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking token");
		}
	}

}
