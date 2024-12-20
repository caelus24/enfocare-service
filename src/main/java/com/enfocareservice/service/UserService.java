package com.enfocareservice.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.enfocareservice.entity.UserEntity;
import com.enfocareservice.model.ChangePasswordRequest;
import com.enfocareservice.model.User;
import com.enfocareservice.model.mapper.UserMapper;
import com.enfocareservice.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserMapper userMapper;

	public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

		var user = (UserEntity) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new IllegalStateException("Wrong password");
		}

		if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
			throw new IllegalStateException("Password are not the same");
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));

		repository.save(user);
	}

	public User findUserByEmail(String email) {
		try {
			UserEntity userEntity = repository.findByEmail(email)
					.orElseThrow(() -> new IllegalArgumentException("User not found"));
			return userMapper.map(userEntity);
		} catch (Exception e) {
			System.err.println("Failed to find user by email " + email + ": " + e.getMessage());
			throw e;
		}
	}

}
