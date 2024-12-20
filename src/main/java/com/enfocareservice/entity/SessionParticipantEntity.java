package com.enfocareservice.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "session_participants")
public class SessionParticipantEntity {

	@EmbeddedId
	private SessionParticipantId id;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("sessionId") // Ensures it uses sessionId from embedded ID
	@JoinColumn(name = "session_id") // This should match the actual database column name in session_participants
										// table
	private ChatSessionEntity chatSession;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId") // Ensures it uses userId from embedded ID
	@JoinColumn(name = "user_id") // This should match the actual database column name in session_participants
									// table
	private UserEntity user;

	private Timestamp joinedAt;

	// Getters and setters
	public SessionParticipantId getId() {
		return id;
	}

	public void setId(SessionParticipantId id) {
		this.id = id;
	}

	public ChatSessionEntity getChatSession() {
		return chatSession;
	}

	public void setChatSession(ChatSessionEntity chatSession) {
		this.chatSession = chatSession;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Timestamp getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Timestamp joinedAt) {
		this.joinedAt = joinedAt;
	}
}
