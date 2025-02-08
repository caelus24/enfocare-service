package com.enfocareservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.enfocareservice.entity.LobbyQueueEntity;
import com.enfocareservice.model.LobbyQueue;
import com.enfocareservice.model.mapper.LobbyQueueMapper;
import com.enfocareservice.repository.LobbyQueueRepository;

@Service
public class LobbyQueueService {

    @Autowired
    private LobbyQueueRepository lobbyQueueRepository;

    @Autowired
    private LobbyQueueMapper lobbyQueueMapper;

    public Integer countEntries(String email) {
        return lobbyQueueRepository.countByDoctor(email);
    }

    public LobbyQueue saveEntry(LobbyQueue lobbyQueue) {
        // Check if the patient is already in the queue for this doctor
        LobbyQueueEntity existingEntry = lobbyQueueRepository.findByDoctorAndPatient(
                lobbyQueue.getDoctor(), lobbyQueue.getPatient());

        if (existingEntry != null) {
            // Return the existing entry if found
            return lobbyQueueMapper.map(existingEntry);
        }

        // If not in the queue, save the new entry
        LobbyQueueEntity lobbyQueueEntity = new LobbyQueueEntity();
        lobbyQueueEntity.setDoctor(lobbyQueue.getDoctor());
        lobbyQueueEntity.setPatient(lobbyQueue.getPatient());
        lobbyQueueEntity.setTimeIn(lobbyQueue.getTimeIn());

        LobbyQueueEntity savedEntity = lobbyQueueRepository.save(lobbyQueueEntity);

        LobbyQueue resLobbyQueue = lobbyQueueMapper.map(savedEntity);
        resLobbyQueue.setId(savedEntity.getId());

        return resLobbyQueue;
    }

    public List<LobbyQueue> getLobbyQueueByDoctor(String doctor) {
        List<LobbyQueue> lobbyQueues = null;
        List<LobbyQueueEntity> lobbyQueueEntities = lobbyQueueRepository.findByDoctor(doctor);

        if (!CollectionUtils.isEmpty(lobbyQueueEntities)) {
            lobbyQueues = lobbyQueueEntities.stream().map(lobbyQueueMapper::map).collect(Collectors.toList());
        }
        return lobbyQueues;
    }

    public LobbyQueue selectEntityByDoctorAndPatien(String doctor, String patient) {
        LobbyQueue lobbyQueue = null;
        LobbyQueueEntity lobbyQueueEntity = lobbyQueueRepository.findByDoctorAndPatient(doctor, patient);

        if (lobbyQueueEntity != null) {
            lobbyQueue = lobbyQueueMapper.map(lobbyQueueEntity);
        }
        return lobbyQueue;
    }

    public void deleteEntityByDoctorAndPatient(String doctor, String patient) {
        LobbyQueueEntity lobbyQueueEntity = lobbyQueueRepository.findByDoctorAndPatient(doctor, patient);
        
        if (lobbyQueueEntity != null) {
            lobbyQueueRepository.delete(lobbyQueueEntity);
        }

        int countAfterDeletion = lobbyQueueRepository.countByDoctor(doctor);
        if (countAfterDeletion == 0) {
            System.out.println("No entities found for email: " + doctor);
        }
    }
}
