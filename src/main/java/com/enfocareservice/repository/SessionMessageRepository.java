package com.enfocareservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.MessageEntity;
import com.enfocareservice.entity.SessionMessageEntity;
import com.enfocareservice.entity.SessionMessageId;

@Repository
public interface SessionMessageRepository extends JpaRepository<SessionMessageEntity, SessionMessageId> {

	@Query("SELECT sme.message FROM SessionMessageEntity sme WHERE sme.chatSession.sessionId = :sessionId")
	List<MessageEntity> findMessagesBySessionId(@Param("sessionId") Integer sessionId);
}