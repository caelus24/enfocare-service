package com.enfocareservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.ProfileEntity;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

	ProfileEntity findByEmail(String email);

	ProfileEntity findByMedicalField(String medicalField);

	ProfileEntity findByPhone(String phoneNumber);

}
