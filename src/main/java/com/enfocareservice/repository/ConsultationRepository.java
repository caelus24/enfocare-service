package com.enfocareservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.ConsultationEntity;

@Repository
public interface ConsultationRepository extends JpaRepository<ConsultationEntity, Long> {

	@Query("SELECT COUNT(c) FROM ConsultationEntity c WHERE c.doctor = ?1 AND YEAR(c.date) = YEAR(CURRENT_DATE) AND MONTH(c.date) = MONTH(CURRENT_DATE)")
	long countCurrentMonthConsultationsByDoctor(String doctor);

	List<ConsultationEntity> findByDoctor(String doctor);

	List<ConsultationEntity> findByPatient(String patient);

}
