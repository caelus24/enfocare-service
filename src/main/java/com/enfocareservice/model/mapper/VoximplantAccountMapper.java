package com.enfocareservice.model.mapper;

import org.springframework.stereotype.Component;

import com.enfocareservice.entity.VoximplantAccountEntity;
import com.enfocareservice.model.VoximplantAccount;

@Component
public class VoximplantAccountMapper {

	public VoximplantAccount map(VoximplantAccountEntity voximplantAccountEntity) {

		VoximplantAccount voximplantAccount = new VoximplantAccount();

		voximplantAccount.setId(voximplantAccountEntity.getId());
		voximplantAccount.setUser(voximplantAccountEntity.getUser());
		voximplantAccount.setVoxId(voximplantAccountEntity.getVoxId());
		voximplantAccount.setVoxPw(voximplantAccountEntity.getVoxPw());
		voximplantAccount.setVoxUsername(voximplantAccountEntity.getVoxUsername());

		return voximplantAccount;
	}

}
