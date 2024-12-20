package com.enfocareservice.model.mapper;

import org.springframework.stereotype.Component;

import com.enfocareservice.entity.LobbyQueueEntity;
import com.enfocareservice.model.LobbyQueue;

@Component
public class LobbyQueueMapper {

	public LobbyQueue map(LobbyQueueEntity lobbyQueueEntity) {

		LobbyQueue lobbyQueue = new LobbyQueue();

		lobbyQueue.setDoctor(lobbyQueueEntity.getDoctor());
		lobbyQueue.setPatient(lobbyQueueEntity.getPatient());
		lobbyQueue.setTimeIn(lobbyQueueEntity.getTimeIn());
		lobbyQueue.setQueue(lobbyQueueEntity.getQueue());

		return lobbyQueue;

	}

}
