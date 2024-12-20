package com.enfocareservice.model.mapper;

import org.springframework.stereotype.Component;

import com.enfocareservice.entity.MedicalFileEntity;
import com.enfocareservice.model.MedicalFile;

@Component
public class MedicalFileMapper {

	public MedicalFile map(MedicalFileEntity entity) {
		MedicalFile model = new MedicalFile();
		model.setId(entity.getId());
		model.setPatientEmail(entity.getPatientEmail());
		model.setDoctorEmail(entity.getDoctorEmail());
		model.setFilePath(entity.getFilePath());
		model.setPassword(entity.getPassword());
		model.setConsultationId(entity.getConsultationId());
		return model;
	}

}
