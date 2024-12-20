package com.enfocareservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.VoximplantAccountEntity;

@Repository
public interface VoximplantAccountRepository extends JpaRepository<VoximplantAccountEntity, Long> {

	VoximplantAccountEntity findByUser(String email);

}
