package com.enfocareservice.model.mapper;

import com.enfocareservice.entity.UserStatusEntity;
import com.enfocareservice.model.UserStatus;

public class UserStatusMapper {

	public UserStatus map(UserStatusEntity userStatusEntity) {
		UserStatus userStatus = new UserStatus();

		userStatus.setEmail(userStatusEntity.getEmail());
		userStatus.setIsOnline(userStatusEntity.getIsOnline());

		return userStatus;
	}

}
