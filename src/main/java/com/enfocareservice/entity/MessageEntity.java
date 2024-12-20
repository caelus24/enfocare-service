package com.enfocareservice.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class MessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "session_id", referencedColumnName = "session_id")
	private ChatSessionEntity session;

	@ManyToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "user_id")
	private UserEntity sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
	private UserEntity receiver;

	@Column(nullable = false)
	private String messageText;

	private Timestamp timestamp;

	public ChatSessionEntity getSession() {
		return session;
	}

	public void setSession(ChatSessionEntity session) {
		this.session = session;
	}

	public UserEntity getSender() {
		return sender;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSender(UserEntity sender) {
		this.sender = sender;
	}

	public UserEntity getReceiver() {
		return receiver;
	}

	public void setReceiver(UserEntity receiver) {
		this.receiver = receiver;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
