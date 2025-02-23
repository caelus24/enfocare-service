package com.enfocareservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.enfocareservice.entity.MedicalFileEntity;

@Repository
public interface MedicalFileRepository extends JpaRepository<MedicalFileEntity, Long> {
	 /**
     * Get a distinct list of patient emails who have uploaded medical files for a specific doctor.
     */
    @Query(value = "SELECT DISTINCT patient_email FROM medical_file WHERE doctor_email = :doctorEmail", nativeQuery = true)
    List<String> findDistinctPatientEmailsByDoctorEmail(@Param("doctorEmail") String doctorEmail);

    /**
     * Retrieve file paths associated with a patient's email.
     */
    @Query(value = "SELECT file_path FROM medical_file WHERE patient_email = :patientEmail", nativeQuery = true)
    List<String> findFilePathsByPatientEmail(@Param("patientEmail") String patientEmail);

    /**
     * Retrieve all medical files related to a specific consultation ID.
     */
    List<MedicalFileEntity> findByConsultationId(Long consultationId);

    /**
     * Retrieve all medical files related to a specific consultation ID and doctor email.
     */
    List<MedicalFileEntity> findByConsultationIdAndDoctorEmail(Long consultationId, String doctorEmail);
}
