package com.enfocareservice.model;

import java.util.Set;

public class CreateSessionRequest {
	private String sessionName;
	private Set<Integer> participantIds;

	// Constructors
	public CreateSessionRequest() {
	}

	public CreateSessionRequest(String sessionName, Set<Integer> participantIds) {
		this.sessionName = sessionName;
		this.participantIds = participantIds;
	}

	// Getters and Setters
	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public Set<Integer> getParticipantIds() {
		return participantIds;
	}

	public void setParticipantIds(Set<Integer> participantIds) {
		this.participantIds = participantIds;
	}
}