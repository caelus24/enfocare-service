package com.enfocareservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.LobbyEntity;

@Repository
public interface LobbyRepository extends JpaRepository<LobbyEntity, Long> {

	LobbyEntity findByEmail(String email);

}
