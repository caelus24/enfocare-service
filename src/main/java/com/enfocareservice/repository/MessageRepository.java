package com.enfocareservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.ChatSessionEntity;
import com.enfocareservice.entity.MessageEntity;
import com.enfocareservice.entity.UserEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
	// Find messages in a session where the participant is either the sender or
	// receiver
	@Query("SELECT m FROM MessageEntity m WHERE m.session = :session AND (m.sender = :participant OR m.receiver = :participant)")
	List<MessageEntity> findMessagesBySessionAndParticipant(@Param("session") ChatSessionEntity session,
			@Param("participant") UserEntity participant);
}
