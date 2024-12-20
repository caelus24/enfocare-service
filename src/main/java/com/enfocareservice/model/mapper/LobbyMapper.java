package com.enfocareservice.model.mapper;

import org.springframework.stereotype.Component;

import com.enfocareservice.entity.LobbyEntity;
import com.enfocareservice.model.Lobby;

@Component
public class LobbyMapper {

	public Lobby map(LobbyEntity lobbyEntity) {

		Lobby lobby = new Lobby();

		lobby.setEmail(lobbyEntity.getEmail());
		lobby.setDoctor(lobbyEntity.getDoctor());
		lobby.setMaxCapacity(lobbyEntity.getMaxCapacity());

		return lobby;
	}

}
