package com.enfocareservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.UserStatusEntity;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatusEntity, Long> {

	UserStatusEntity findByEmail(String email);

}
