package com.enfocareservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.enfocareservice.entity.SessionMessageEntity;
import com.enfocareservice.model.Message;
import com.enfocareservice.service.ChatService;

@Controller
public class WebSocketMessageController {

	@Autowired
	private ChatService chatService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/enfocare/chat/send")
	public void receiveMessageFromClient(@Payload Message message) throws Exception {

		System.err.println(message.getSenderId() + " " + message.getReceiverId());

		System.out.println(" receiveMessageFromClient Received message: " + message);

		if (message.getSessionId() == null) {
			throw new IllegalArgumentException("Session ID is required.");
		}
		Integer sessionId = message.getSessionId().intValue(); // Convert Long to Integer
		SessionMessageEntity res = chatService.sendMessage(sessionId, message.getSenderId(), message.getReceiverId(),
				message.getMessageText());

		String destination = String.format("/messages/%d/user/%d", message.getSessionId(), message.getSenderId());
		System.err.println("Sending messages to destination: " + destination);
		message.setMessageId(res.getMessage().getId());
		messagingTemplate.convertAndSend(destination, message);

	}

	@MessageMapping("/enfocare/chat/fetch")
	public void fetchMessagesForSession(@Payload Message message) {
		System.out.println("Received fetch request with sessionId: " + message.getSessionId() + ", senderId: "
				+ message.getSenderId());

		if (message.getSessionId() == null || message.getSenderId() == null) {
			throw new IllegalArgumentException("Session ID and Sender ID are required.");
		}

		try {
			List<Message> messages = chatService.getMessagesForSessionAndParticipant(message.getSessionId().intValue(),
					message.getSenderId().intValue());
			String destination = String.format("/messages/%d/user/%d", message.getSessionId(), message.getSenderId());
			System.err.println("Sending messages to destination: " + destination);
			messagingTemplate.convertAndSend(destination, messages);

			messages.forEach(e -> {
				System.err.println(e);
			});
		} catch (Exception e) {
			System.out.println("Error while fetching messages: " + e.getMessage());
			throw new RuntimeException("Failed to fetch messages", e);
		}
	}

}