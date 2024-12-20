package com.enfocareservice.model;

public class DeleteQueueRequest {

	private String doctor;
	private String patient;

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

}
