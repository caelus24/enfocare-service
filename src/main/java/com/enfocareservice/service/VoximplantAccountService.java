package com.enfocareservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enfocareservice.entity.VoximplantAccountEntity;
import com.enfocareservice.model.VoximplantAccount;
import com.enfocareservice.model.mapper.VoximplantAccountMapper;
import com.enfocareservice.repository.VoximplantAccountRepository;

@Service
public class VoximplantAccountService {

    private static final Logger logger = LoggerFactory.getLogger(VoximplantAccountService.class);

    @Autowired
    private VoximplantAccountRepository voximplantAccountRepository;

    @Autowired
    private VoximplantAccountMapper voximplantAccountMapper;

    public VoximplantAccount createAccount(VoximplantAccount voximplantAccount) {
        logger.info("Creating Voximplant account for user: {} TUNAMAYO", voximplantAccount.getUser());

        VoximplantAccountEntity toSave = new VoximplantAccountEntity();
        toSave.setUser(voximplantAccount.getUser());
        toSave.setVoxId(voximplantAccount.getVoxId());
        toSave.setVoxPw(voximplantAccount.getVoxPw());
        toSave.setVoxUsername(voximplantAccount.getVoxUsername());

        VoximplantAccountEntity savedEntity = voximplantAccountRepository.save(toSave);
        logger.info("Voximplant account successfully created with ID: {} TUNAMAYO", savedEntity.getId());

        return voximplantAccountMapper.map(savedEntity);
    }

    public VoximplantAccount getAccountByEmail(String email) {
        logger.info("Fetching Voximplant account for email: {}", email);

        VoximplantAccountEntity entity = voximplantAccountRepository.findByUser(email);
        if (entity == null) {
            logger.error("No Voximplant account found for email: {} TUNAMAYO", email);
            return null;
        }

        logger.info("Voximplant account found for email: {} TUNAMAYO", email);
        return voximplantAccountMapper.map(entity);
    }
}
