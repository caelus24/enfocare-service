package com.enfocareservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.LobbyQueueEntity;

@Repository
public interface LobbyQueueRepository extends JpaRepository<LobbyQueueEntity, Long> {

	Integer countByDoctor(String email);

	void deleteByDoctorAndPatient(String doctor, String patient);

	LobbyQueueEntity findByDoctorAndPatient(String doctor, String patient);

	List<LobbyQueueEntity> findByDoctor(String doctor);

}
