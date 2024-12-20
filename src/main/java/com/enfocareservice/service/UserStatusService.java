package com.enfocareservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enfocareservice.entity.UserStatusEntity;
import com.enfocareservice.repository.UserStatusRepository;

@Service
public class UserStatusService {

	@Autowired
	private UserStatusRepository userStatusRepository;

	public void setUserOnline(String email) {

		System.err.println("SETUSEONLINE CALLED");
		UserStatusEntity userStatusEntity = userStatusRepository.findByEmail(email);

		System.err.println(userStatusEntity.getId() + " " + userStatusEntity.getIsOnline());

		if (userStatusEntity != null) {

			userStatusEntity.setIsOnline(true);
			userStatusRepository.save(userStatusEntity);
		}

	}

	public void registerUserStatus(String email) {

		UserStatusEntity userStatusEntity = new UserStatusEntity();

		userStatusEntity.setEmail(email);
		userStatusEntity.setIsOnline(false);

		userStatusRepository.save(userStatusEntity);

	}

	public void setUserOffline(String email) {

		System.err.println("SETUSEROFFLINE CALLED");

		UserStatusEntity userStatusEntity = userStatusRepository.findByEmail(email);

		if (userStatusEntity != null) {

			userStatusEntity.setIsOnline(false);
			userStatusRepository.save(userStatusEntity);
		}

	}

}
