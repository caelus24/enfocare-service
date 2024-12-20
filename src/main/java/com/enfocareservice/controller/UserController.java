package com.enfocareservice.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enfocareservice.model.ChangePasswordRequest;
import com.enfocareservice.model.User;
import com.enfocareservice.service.UserService;

@RestController
@RequestMapping("/enfocare/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PatchMapping
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {
		userService.changePassword(request, connectedUser);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{email}")
	public ResponseEntity<User> getUserIdByEmail(@PathVariable String email) {
		System.err.println("getUserIdByEmail called with email: " + email);
		try {
			User user = userService.findUserByEmail(email);

			return ResponseEntity.ok(user);
		} catch (Exception e) {
			System.err.println("Error fetching user: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
