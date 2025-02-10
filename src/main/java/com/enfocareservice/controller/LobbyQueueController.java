package com.enfocareservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.enfocareservice.model.DeleteQueueRequest;
import com.enfocareservice.model.LobbyQueue;
import com.enfocareservice.service.LobbyQueueService;

@RestController
@RequestMapping("/enfocare/queue")
public class LobbyQueueController {

    @Autowired
    private LobbyQueueService lobbyQueueService;

    @GetMapping("/count")
    public ResponseEntity<Integer> countEntries(@RequestParam String email) {
        Integer count = lobbyQueueService.countEntries(email);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<LobbyQueue> saveEntry(@RequestBody LobbyQueue lobbyQueue) {
        System.err.println(lobbyQueue.getTimeIn());
        LobbyQueue savedQueue = lobbyQueueService.saveEntry(lobbyQueue);
        return new ResponseEntity<>(savedQueue, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEntityByDoctorAndPatient(@RequestBody DeleteQueueRequest deleteQueueRequest) {
        lobbyQueueService.deleteEntityByDoctorAndPatient(deleteQueueRequest.getDoctor(), deleteQueueRequest.getPatient());
        return new ResponseEntity<>("Entity deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<LobbyQueue>> getLobbyQueueByDoctor(@RequestParam String doctor) {
        List<LobbyQueue> lobbyQueues = lobbyQueueService.getLobbyQueueByDoctor(doctor);

        return (doctor == null || doctor.isEmpty()) ? ResponseEntity.badRequest().build()
                : ResponseEntity.status(CollectionUtils.isEmpty(lobbyQueues) ? HttpStatus.NOT_FOUND : HttpStatus.OK)
                        .body(lobbyQueues);
    }

    // New endpoint for picking up a patient (removing from queue)
    @DeleteMapping("/pickup")
    public ResponseEntity<String> pickUpPatient(@RequestParam String doctor, @RequestParam String patient) {
        lobbyQueueService.pickUpPatient(doctor, patient);
        return new ResponseEntity<>("Patient " + patient + " has been removed from the queue.", HttpStatus.OK);
    }
}
