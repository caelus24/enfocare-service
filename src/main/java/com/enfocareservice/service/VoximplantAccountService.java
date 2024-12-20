package com.enfocareservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enfocareservice.entity.VoximplantAccountEntity;
import com.enfocareservice.model.VoximplantAccount;
import com.enfocareservice.model.mapper.VoximplantAccountMapper;
import com.enfocareservice.repository.VoximplantAccountRepository;

@Service
public class VoximplantAccountService {

	@Autowired
	private VoximplantAccountRepository voximplantAccountRepository;

	@Autowired
	private VoximplantAccountMapper voximplantAccountMapper;

	public VoximplantAccount createAccount(VoximplantAccount voximplantAccount) {

		System.err.println();

		VoximplantAccountEntity toSave = new VoximplantAccountEntity();

		toSave.setUser(voximplantAccount.getUser());
		toSave.setVoxId(voximplantAccount.getVoxId());
		toSave.setVoxPw(voximplantAccount.getVoxPw());
		toSave.setVoxUsername(voximplantAccount.getVoxUsername());

		return voximplantAccountMapper.map(voximplantAccountRepository.save(toSave));

	}

	public VoximplantAccount getAccountByEmail(String email) {

		System.err.println("getAccountByEmail " + email);
		return voximplantAccountMapper.map(voximplantAccountRepository.findByUser(email));
	}

}
