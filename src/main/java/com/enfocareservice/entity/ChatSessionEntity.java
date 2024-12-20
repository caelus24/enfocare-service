package com.enfocareservice.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_sessions")
public class ChatSessionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "session_id")
	private Long sessionId;

	private String sessionName;
	private Timestamp createdAt;

	@JsonManagedReference
	@OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SessionParticipantEntity> participants = new HashSet<>();

	// Getters and setters

	public String getSessionName() {
		return sessionName;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Set<SessionParticipantEntity> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<SessionParticipantEntity> participants) {
		this.participants = participants;
	}
}
