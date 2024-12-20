package com.enfocareservice.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class SessionMessageId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long sessionId;
	private Long messageId;

	// Default constructor
	public SessionMessageId() {
	}

	// All-arguments constructor
	public SessionMessageId(Long sessionId, Long messageId) {
		this.sessionId = sessionId;
		this.messageId = messageId;
	}

	// Getters and Setters

	// Overriding equals and hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SessionMessageId that = (SessionMessageId) o;
		return Objects.equals(sessionId, that.sessionId) && Objects.equals(messageId, that.messageId);
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sessionId, messageId);
	}
}
