package com.enfocareservice.repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * Retrieve all medical files related to a specific consultation ID and patient email.
     */
    List<MedicalFileEntity> findByConsultationIdAndPatientEmail(Long consultationId, String patientEmail);

    /**
     * Retrieve a specific medical file by patient email and filename.
     */
    @Query(value = "SELECT * FROM medical_file WHERE patient_email = :patientEmail AND file_path = :fileName", nativeQuery = true)
    Optional<MedicalFileEntity> getFileByPatientAndFileName(@Param("patientEmail") String patientEmail, @Param("fileName") String fileName);

    /**
     * Retrieve a medical file by its unique ID.
     */
    @Query(value = "SELECT * FROM medical_file WHERE id = :fileId", nativeQuery = true)
    Optional<MedicalFileEntity> getFileById(@Param("fileId") Long fileId);
}