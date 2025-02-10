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
        // Remove existing entries for the patient before adding a new one
        lobbyQueueRepository.deleteByPatient(lobbyQueue.getPatient());

        // Check if the patient is already in the queue for this doctor
        LobbyQueueEntity existingEntry = lobbyQueueRepository.findByDoctorAndPatient(
                lobbyQueue.getDoctor(), lobbyQueue.getPatient());

        if (existingEntry != null) {
            return lobbyQueueMapper.map(existingEntry); // Return the existing entry
        }

        // If not in the queue, save the new entry
        LobbyQueueEntity lobbyQueueEntity = new LobbyQueueEntity();
        lobbyQueueEntity.setDoctor(lobbyQueue.getDoctor());
        lobbyQueueEntity.setPatient(lobbyQueue.getPatient());
        lobbyQueueEntity.setTimeIn(lobbyQueue.getTimeIn());

        LobbyQueueEntity savedEntity = lobbyQueueRepository.save(lobbyQueueEntity);
        return lobbyQueueMapper.map(savedEntity);
    }

    public List<LobbyQueue> getLobbyQueueByDoctor(String doctor) {
        List<LobbyQueueEntity> lobbyQueueEntities = lobbyQueueRepository.findByDoctor(doctor);

        if (!CollectionUtils.isEmpty(lobbyQueueEntities)) {
            return lobbyQueueEntities.stream().map(lobbyQueueMapper::map).collect(Collectors.toList());
        }
        return List.of();
    }

    public LobbyQueue selectEntityByDoctorAndPatient(String doctor, String patient) {
        LobbyQueueEntity lobbyQueueEntity = lobbyQueueRepository.findByDoctorAndPatient(doctor, patient);
        return (lobbyQueueEntity != null) ? lobbyQueueMapper.map(lobbyQueueEntity) : null;
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

    public void pickUpPatient(String doctor, String patient) {
        // Remove the patient from the queue
        LobbyQueueEntity lobbyQueueEntity = lobbyQueueRepository.findByDoctorAndPatient(doctor, patient);

        if (lobbyQueueEntity != null) {
            lobbyQueueRepository.delete(lobbyQueueEntity);
            System.out.println("Patient " + patient + " removed from queue after being picked up.");
        } else {
            System.out.println("Patient not found in queue.");
        }
    }
}
