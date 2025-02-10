package com.enfocareservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.enfocareservice.entity.LobbyQueueEntity;

@Repository
public interface LobbyQueueRepository extends JpaRepository<LobbyQueueEntity, Long> {

    Integer countByDoctor(String email);

    void deleteByDoctorAndPatient(String doctor, String patient);

    LobbyQueueEntity findByDoctorAndPatient(String doctor, String patient);

    List<LobbyQueueEntity> findByDoctor(String doctor);

    @Modifying
    @Transactional
    @Query("DELETE FROM LobbyQueueEntity l WHERE l.patient = :patient")
    void deleteByPatient(@Param("patient") String patient);
}
