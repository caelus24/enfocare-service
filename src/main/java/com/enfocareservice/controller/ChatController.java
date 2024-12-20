package com.enfocareservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enfocareservice.entity.ChatSessionEntity;
import com.enfocareservice.model.ChatSession;
import com.enfocareservice.model.CreateSessionRequest;
import com.enfocareservice.model.Message;
import com.enfocareservice.model.mapper.ChatAppMapper;
import com.enfocareservice.service.ChatService;

@RestController
@RequestMapping("/enfocare/chat")
public class ChatController {

	@Autowired
	private ChatService chatService;

	@Autowired
	private ChatAppMapper chatAppMapper;

	@PostMapping("/send")
	public ResponseEntity<?> sendMessage(@RequestParam Integer sessionId, @RequestParam Integer senderId,
			@RequestParam Integer receiverId, @RequestParam String messageText) {
		try {
			chatService.sendMessage(sessionId, senderId, receiverId, messageText);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (IllegalArgumentException e) {
			System.err.println("Bad request: " + e);
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			System.err.println("Internal server error: " + e);
			return ResponseEntity.internalServerError().body("An error occurred while sending the message.");
		}
	}

	@GetMapping("/messages")
	public ResponseEntity<List<Message>> getMessagesForSession(@RequestParam Integer sessionId,
			@RequestParam Integer participantId) {
		try {
			List<Message> messages = chatService.getMessagesForSessionAndParticipant(sessionId, participantId);
			return ResponseEntity.ok(messages);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@PostMapping("/sessions/create")
	public ResponseEntity<ChatSessionEntity> createSession(@RequestBody CreateSessionRequest request) {
		ChatSessionEntity createdSession = chatService.createSession(request.getSessionName(),
				request.getParticipantIds());
		return ResponseEntity.ok(createdSession);
	}

	@GetMapping("/sessions/latest")
	public ResponseEntity<ChatSession> getLatestSessionByParticipants(
			@RequestParam("participantIds") String participantIds) {

		Set<Integer> ids = Arrays.stream(participantIds.split(",")).map(String::trim).map(Integer::parseInt)
				.collect(Collectors.toSet());

		ChatSessionEntity session = chatService.fetchLatestSessionByParticipants(ids);

		ChatSession chatSession = chatAppMapper.chatSessionEntityToModel(session);

		return ResponseEntity.ok(chatSession);
	}

}