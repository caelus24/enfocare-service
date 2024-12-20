package com.enfocareservice.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "session_messages")
public class SessionMessageEntity {

	@EmbeddedId
	private SessionMessageId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("sessionId")
	@JoinColumn(name = "session_id") // This should map to the actual column name in the DB
	private ChatSessionEntity chatSession;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("messageId")
	@JoinColumn(name = "message_id")
	private MessageEntity message;

	public SessionMessageId getId() {
		return id;
	}

	public void setId(SessionMessageId id) {
		this.id = id;
	}

	public ChatSessionEntity getChatSession() {
		return chatSession;
	}

	public void setChatSession(ChatSessionEntity chatSession) {
		this.chatSession = chatSession;
	}

	public MessageEntity getMessage() {
		return message;
	}

	public void setMessage(MessageEntity message) {
		this.message = message;
	}

}
