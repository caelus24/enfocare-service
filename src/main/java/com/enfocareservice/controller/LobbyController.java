package com.enfocareservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enfocareservice.model.Lobby;
import com.enfocareservice.service.LobbyService;

@RestController
@RequestMapping("/enfocare/lobby")
public class LobbyController {

	@Autowired
	private LobbyService lobbyService;

	@PostMapping("/save")
	public ResponseEntity<Lobby> saveLobby(@RequestBody Lobby lobby) {

		ResponseEntity<Lobby> response = null;

		Lobby result = lobbyService.saveLobby(lobby);

		if (lobby == null) {
			response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			response = ResponseEntity.ok(result);
		}

		return response;

	}

	@GetMapping("/{email}")
	public ResponseEntity<Lobby> getByEmail(@PathVariable String email) {

		ResponseEntity<Lobby> response = null;

		Lobby result = lobbyService.getLobbyByEmail(email);

		if (result == null) {
			response = ResponseEntity.status(HttpStatus.NOT_EXTENDED).build();
		} else {
			response = ResponseEntity.ok(result);
		}

		return response;

	}

}
