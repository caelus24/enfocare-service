package com.enfocareservice.model;

public class SessionMessage {
	private Integer sessionId;
	private Integer messageId;

	// Constructors, Getters, and Setters

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	// toString, equals, and hashCode methods
}
