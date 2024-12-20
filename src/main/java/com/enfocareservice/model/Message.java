package com.enfocareservice.model;

import java.time.LocalDateTime;

public class Message {
	private Long messageId;
	private Integer senderId;
	private Long sessionId;
	private Integer receiverId;
	private String messageText;
	private LocalDateTime timestamp;

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Integer getSenderId() {
		return senderId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public Integer getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", senderId=" + senderId + ", sessionId=" + sessionId
				+ ", receiverId=" + receiverId + ", messageText=" + messageText + ", timestamp=" + timestamp + "]";
	}

}