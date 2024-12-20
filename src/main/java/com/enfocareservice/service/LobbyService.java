package com.enfocareservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enfocareservice.entity.LobbyEntity;
import com.enfocareservice.model.Lobby;
import com.enfocareservice.model.mapper.LobbyMapper;
import com.enfocareservice.repository.LobbyRepository;

@Service
public class LobbyService {

	@Autowired
	private LobbyRepository lobbyRepository;

	@Autowired
	private LobbyMapper lobbyMapper;

	public Lobby saveLobby(Lobby lobby) {

		Lobby result = new Lobby();

		LobbyEntity lobbyEntity = new LobbyEntity();

		lobbyEntity.setEmail(lobby.getEmail());
		lobbyEntity.setDoctor(lobby.getDoctor());
		lobbyEntity.setMaxCapacity(lobby.getMaxCapacity());

		result = lobbyMapper.map(lobbyRepository.save(lobbyEntity));

		return result;
	}

	public Lobby getLobbyByEmail(String email) {

		return lobbyMapper.map(lobbyRepository.findByEmail(email));

	}

}
