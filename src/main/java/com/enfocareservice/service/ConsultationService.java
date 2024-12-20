package com.enfocareservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enfocareservice.entity.ConsultationEntity;
import com.enfocareservice.model.Consultation;
import com.enfocareservice.model.mapper.ConsultationMapper;
import com.enfocareservice.repository.ConsultationRepository;

@Service
public class ConsultationService {

	@Autowired
	private ConsultationMapper consultationMapper;

	@Autowired
	private ConsultationRepository consultationRepository;

	public Consultation save(Consultation consultation) {

		ConsultationEntity consultationEntity = new ConsultationEntity();

		consultationEntity.setAilment(consultation.getAilment());
		consultationEntity.setDate(consultation.getDate());
		consultationEntity.setDiagnosis(consultation.getDiagnosis());
		consultationEntity.setDoctor(consultation.getDoctor());
		consultationEntity.setPatient(consultation.getPatient());
		consultationEntity.setSymptoms(consultation.getSymptoms());
		consultationEntity.setTreatment(consultation.getTreatment());

		return consultationMapper.map(consultationRepository.save(consultationEntity));
	}

	public List<Consultation> findByDoctor(String doctor) {
		List<ConsultationEntity> entities = consultationRepository.findByDoctor(doctor);
		return entities.stream().map(entity -> consultationMapper.map(entity)).collect(Collectors.toList());
	}

	public List<Consultation> findByPatient(String patient) {
		List<ConsultationEntity> entities = consultationRepository.findByPatient(patient);
		return entities.stream().map(entity -> consultationMapper.map(entity)).collect(Collectors.toList());
	}

	public Consultation editConsultation(Long id, Consultation consultation) {
		Optional<ConsultationEntity> optionalConsultationEntity = consultationRepository.findById(id);
		if (optionalConsultationEntity.isPresent()) {
			ConsultationEntity consultationEntity = optionalConsultationEntity.get();
			if (consultation.getAilment() != null) {
				consultationEntity.setAilment(consultation.getAilment());
			}
			if (consultation.getDate() != null) {
				consultationEntity.setDate(consultation.getDate());
			}
			if (consultation.getDiagnosis() != null) {
				consultationEntity.setDiagnosis(consultation.getDiagnosis());
			}
			if (consultation.getDoctor() != null) {
				consultationEntity.setDoctor(consultation.getDoctor());
			}
			if (consultation.getPatient() != null) {
				consultationEntity.setPatient(consultation.getPatient());
			}
			if (consultation.getSymptoms() != null) {
				consultationEntity.setSymptoms(consultation.getSymptoms());
			}
			if (consultation.getTreatment() != null) {
				consultationEntity.setTreatment(consultation.getTreatment());
			}
			return consultationMapper.map(consultationRepository.save(consultationEntity));
		} else {
			return null;
		}
	}

	public long getCurrentMonthConsultationCountByDoctor(String doctor) {
		return consultationRepository.countCurrentMonthConsultationsByDoctor(doctor);
	}

}
