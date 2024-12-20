package com.enfocareservice.model.mapper;

import org.springframework.stereotype.Component;

import com.enfocareservice.entity.UserEntity;
import com.enfocareservice.model.User;

@Component
public class UserMapper {

	public User map(UserEntity userEntity) {

		User user = new User();

		user.setEmail(userEntity.getEmail());
		user.setPassword(userEntity.getPassword());
		user.setId(userEntity.getId());

		return user;
	}

}
