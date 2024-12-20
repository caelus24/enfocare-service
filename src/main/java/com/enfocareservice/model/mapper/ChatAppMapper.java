package com.enfocareservice.model.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Component;

import com.enfocareservice.entity.ChatSessionEntity;
import com.enfocareservice.entity.MessageEntity;
import com.enfocareservice.entity.SessionParticipantEntity;
import com.enfocareservice.model.ChatSession;
import com.enfocareservice.model.Message;
import com.enfocareservice.model.SessionParticipant;

@Component
public class ChatAppMapper {

	public Message mapToMessageModel(MessageEntity messageEntity) {
		Message messageModel = new Message();
		messageModel.setMessageId(messageEntity.getId());
		messageModel.setSenderId(messageEntity.getSender().getId().intValue()); // Adjust as needed
		messageModel.setReceiverId(messageEntity.getReceiver().getId().intValue()); // Adjust as needed
		messageModel.setMessageText(messageEntity.getMessageText());
		messageModel.setTimestamp(messageEntity.getTimestamp().toLocalDateTime());
		messageModel.setSessionId(messageEntity.getSession().getSessionId());
		return messageModel;
	}

	public ChatSession chatSessionEntityToModel(ChatSessionEntity entity) {
		ChatSession model = new ChatSession();
		model.setSessionId(entity.getSessionId());
		model.setSessionName(entity.getSessionName());
		model.setCreatedAt(LocalDateTime.ofInstant(entity.getCreatedAt().toInstant(), ZoneId.systemDefault()));
		return model;
	}

	public SessionParticipant sessionParticipantEntityToModel(SessionParticipantEntity entity) {
		SessionParticipant model = new SessionParticipant();
		model.setSessionId(entity.getChatSession().getSessionId());
		model.setUserId(entity.getUser().getId()); // Adjust as needed
		model.setJoinedAt(LocalDateTime.ofInstant(entity.getJoinedAt().toInstant(), ZoneId.systemDefault()));
		return model;
	}

//	public SessionMessage sessionMessageEntityToModel(SessionMessageEntity entity) {
//		SessionMessage model = new SessionMessage();
//		model.setSessionId(entity.getChatSession().getSessionId());
//		model.setMessageId(entity.getMessage().getMessageId());
//		return model;
//	}

}
