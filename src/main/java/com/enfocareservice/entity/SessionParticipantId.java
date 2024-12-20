package com.enfocareservice.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class SessionParticipantId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "session_id")
	private Long sessionId;

	@Column(name = "user_id")
	private Long userId;

	// Constructors, getters, setters
	public SessionParticipantId() {
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SessionParticipantId that = (SessionParticipantId) o;
		return sessionId != null && sessionId.equals(that.sessionId) && userId != null && userId.equals(that.userId);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(sessionId, userId);
	}
}
