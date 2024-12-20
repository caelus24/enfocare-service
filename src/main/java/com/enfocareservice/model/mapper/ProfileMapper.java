package com.enfocareservice.model.mapper;

import org.springframework.stereotype.Component;

import com.enfocareservice.entity.ProfileEntity;
import com.enfocareservice.model.Profile;

@Component
public class ProfileMapper {

	public Profile map(ProfileEntity profileEntity) {
		Profile profile = new Profile();

		profile.setAge(profileEntity.getAge());
		profile.setBiometric(profileEntity.getBiometric());
		profile.setBirthday(profileEntity.getBirthday());
		profile.setBmi(profileEntity.getBmi());
		profile.setClassification(profileEntity.getClassification());
		profile.setEmail(profileEntity.getEmail());
		profile.setFirstname(profileEntity.getFirstname());
		profile.setLastname(profileEntity.getLastname());
		profile.setGender(profileEntity.getGender());
		profile.setHeight(profileEntity.getHeight());
		profile.setIsDoctor(profileEntity.getIsDoctor());
		profile.setMedicalField(profileEntity.getMedicalField());
		profile.setPhone(profileEntity.getPhone());
		profile.setProfileSetup(profileEntity.getProfileSetup());
		profile.setWeight(profileEntity.getWeight());
		profile.setBloodType(profileEntity.getBloodType());
		profile.setAvatarDirectory(profileEntity.getAvatarDirectory());
		return profile;

	}

}
