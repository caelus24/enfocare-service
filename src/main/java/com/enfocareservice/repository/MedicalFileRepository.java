package com.enfocareservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.MedicalFileEntity;

@Repository
public interface MedicalFileRepository extends JpaRepository<MedicalFileEntity, Long> {

	@Query(value = "SELECT DISTINCT patient_email FROM medical_file WHERE doctor_email = :doctorEmail", nativeQuery = true)
	List<String> findDistinctPatientEmailsByDoctorEmail(@Param("doctorEmail") String doctorEmail);

	@Query(value = "SELECT file_path FROM enfocare.medical_file WHERE patient_email = :patientEmail", nativeQuery = true)
	List<String> findFilePathsByPatientEmail(String patientEmail);

	List<MedicalFileEntity> findByConsultationId(Long consultationId);

}
