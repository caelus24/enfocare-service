package com.enfocareservice.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enfocareservice.entity.ChatSessionEntity;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSessionEntity, Integer> {

	@Query("SELECT cs FROM ChatSessionEntity cs JOIN cs.participants p WHERE p.user.id IN :participantIds GROUP BY cs HAVING COUNT(p) = :size ORDER BY cs.createdAt DESC")
	List<ChatSessionEntity> findLatestSessionByParticipants(@Param("participantIds") Set<Integer> participantIds,
			@Param("size") long size);
}
