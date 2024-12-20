package com.enfocareservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.SessionParticipantEntity;
import com.enfocareservice.entity.SessionParticipantId;

@Repository
public interface SessionParticipantRepository extends JpaRepository<SessionParticipantEntity, SessionParticipantId> {
}