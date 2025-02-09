package com.enfocareservice.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enfocareservice.entity.ChatSessionEntity;
import com.enfocareservice.entity.MessageEntity;
import com.enfocareservice.entity.SessionMessageEntity;
import com.enfocareservice.entity.SessionMessageId;
import com.enfocareservice.entity.SessionParticipantEntity;
import com.enfocareservice.entity.SessionParticipantId;
import com.enfocareservice.entity.UserEntity;
import com.enfocareservice.model.Message;
import com.enfocareservice.model.mapper.ChatAppMapper;
import com.enfocareservice.repository.ChatSessionRepository;
import com.enfocareservice.repository.MessageRepository;
import com.enfocareservice.repository.SessionMessageRepository;
import com.enfocareservice.repository.UserRepository;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private SessionMessageRepository sessionMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatAppMapper chatAppMapper;

    @Transactional
    public SessionMessageEntity sendMessage(Integer sessionId, Integer senderId, Integer receiverId,
                                            String messageText) {
        logger.info("sendMessage called with sessionId: {}, senderId: {}, receiverId: {} TUNAMAYO", sessionId, senderId, receiverId);
        try {
            UserEntity sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new IllegalArgumentException("Sender not found with ID: " + senderId));
            UserEntity receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new IllegalArgumentException("Receiver not found with ID: " + receiverId));
            ChatSessionEntity chatSession = chatSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("Chat session does not exist with ID: " + sessionId));

            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setSender(sender);
            messageEntity.setReceiver(receiver);
            messageEntity.setMessageText(messageText);
            messageEntity.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
            messageEntity.setSession(chatSession);
            messageEntity = messageRepository.save(messageEntity);

            SessionMessageEntity sessionMessageEntity = new SessionMessageEntity();
            sessionMessageEntity.setChatSession(chatSession);
            sessionMessageEntity.setMessage(messageEntity);

            long sessIdLong = sessionId;
            sessionMessageEntity.setId(new SessionMessageId(sessIdLong, messageEntity.getId()));

            SessionMessageEntity sessionMessageEntityRes = sessionMessageRepository.save(sessionMessageEntity);
            logger.info("Message sent and recorded with messageId {} TUNAMAYO", messageEntity.getId());
            return sessionMessageEntityRes;
        } catch (Exception e) {
            logger.error("Error sending message TUNAMAYO", e);
            throw e;
        }
    }

    public List<Message> getMessagesForSessionAndParticipant(Integer sessionId, Integer participantId) {
        logger.debug("Fetching messages for session {} and participant {} TUNAMAYO", sessionId, participantId);
        try {
            ChatSessionEntity session = chatSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("Session not found"));
            UserEntity participant = userRepository.findById(participantId)
                    .orElseThrow(() -> new IllegalArgumentException("Participant not found"));

            List<MessageEntity> messageEntities = messageRepository.findMessagesBySessionAndParticipant(session, participant);
            return messageEntities.stream().map(chatAppMapper::mapToMessageModel).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching messages TUNAMAYO", e);
            throw e;
        }
    }

    @Transactional
    public ChatSessionEntity createSession(String sessionName, Set<Integer> participantIds) {
        logger.debug("Creating chat session with name {}", sessionName);
        try {
            ChatSessionEntity chatSession = new ChatSessionEntity();
            chatSession.setSessionName(sessionName);
            chatSession.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

            Set<SessionParticipantEntity> participants = chatSession.getParticipants();
            participants.clear();

            participantIds.forEach(participantId -> {
                UserEntity participant = userRepository.findById(participantId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + participantId));
                SessionParticipantEntity sessionParticipant = new SessionParticipantEntity();
                sessionParticipant.setChatSession(chatSession);
                sessionParticipant.setUser(participant);
                sessionParticipant.setJoinedAt(Timestamp.valueOf(LocalDateTime.now()));

                SessionParticipantId participantIdObj = new SessionParticipantId();
                participantIdObj.setUserId(participant.getId());
                sessionParticipant.setId(participantIdObj);

                participants.add(sessionParticipant);
            });

            ChatSessionEntity savedSession = chatSessionRepository.save(chatSession);
            logger.info("Chat session created with ID {}TUNAMAYO", savedSession.getSessionId());
            return savedSession;
        } catch (Exception e) {
            logger.error("Error creating chat session TUNAMAYO", e);
            throw e;
        }
    }

    public ChatSessionEntity fetchLatestSessionByParticipants(Set<Integer> participantIds) {
        logger.debug("Fetching latest session for participants {}", participantIds);
        try {
            List<ChatSessionEntity> sessions = chatSessionRepository.findLatestSessionByParticipants(participantIds,
                    participantIds.size());
            if (sessions.isEmpty()) {
                logger.warn("No sessions found for given participants");
                throw new IllegalStateException("No session available for the provided participant IDs");
            }
            return sessions.get(0);
        } catch (Exception e) {
            logger.error("Error fetching latest session TUNAMAYO", e);
            throw e;
        }
    }
}
