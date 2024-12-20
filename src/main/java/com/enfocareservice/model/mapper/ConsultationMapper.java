package com.enfocareservice.model.mapper;

import org.springframework.stereotype.Component;

import com.enfocareservice.entity.ConsultationEntity;
import com.enfocareservice.model.Consultation;

@Component
public class ConsultationMapper {

	public Consultation map(ConsultationEntity consultationEntity) {

		Consultation consultation = new Consultation();

		consultation.setId(consultationEntity.getId());
		consultation.setDiagnosis(consultationEntity.getDiagnosis());
		consultation.setAilment(consultationEntity.getAilment());
		consultation.setDate(consultationEntity.getDate());
		consultation.setDoctor(consultationEntity.getDoctor());
		consultation.setPatient(consultationEntity.getPatient());
		consultation.setSymptoms(consultationEntity.getSymptoms());
		consultation.setTreatment(consultationEntity.getTreatment());

		return consultation;
	}

}
