package com.enfocareservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enfocareservice.model.Consultation;
import com.enfocareservice.service.ConsultationService;

@RestController
@RequestMapping("/enfocare/consultation")
public class ConsultationController {

	@Autowired
	private ConsultationService consultationService;

	@PostMapping("/save")
	public ResponseEntity<Consultation> save(@RequestBody Consultation consultation) {

		ResponseEntity<Consultation> responseEntity = null;

		if (consultation == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		Consultation result = consultationService.save(consultation);

		if (result != null) {
			responseEntity = ResponseEntity.ok(result);
		} else {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
		}

		return responseEntity;
	}

	@GetMapping("/doctor/{doctor}")
	public List<Consultation> getConsultationsByDoctor(@PathVariable String doctor) {
		return consultationService.findByDoctor(doctor);
	}

	@GetMapping("/patient/{patient}")
	public List<Consultation> getConsultationsByPatient(@PathVariable String patient) {
		return consultationService.findByPatient(patient);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Consultation> editConsultation(@PathVariable Long id,
			@RequestBody Consultation consultation) {
		ResponseEntity<Consultation> responseEntity = null;

		if (consultation == null || id == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		Consultation updatedConsultation = consultationService.editConsultation(id, consultation);

		if (updatedConsultation != null) {
			responseEntity = ResponseEntity.ok(updatedConsultation);
		} else {
			responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return responseEntity;
	}

	@GetMapping("/doctor/{doctor}/current-month-count")
	public ResponseEntity<Long> getCurrentMonthConsultationCountByDoctor(@PathVariable String doctor) {
		long count = consultationService.getCurrentMonthConsultationCountByDoctor(doctor);
		return ResponseEntity.ok(count);
	}
}
