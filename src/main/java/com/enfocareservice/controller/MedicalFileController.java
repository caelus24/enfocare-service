package com.enfocareservice.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enfocareservice.model.MedicalFile;
import com.enfocareservice.service.MedicalFileService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/enfocare/medical-file")
public class MedicalFileController {

    @Autowired
    private MedicalFileService medicalFileService;

    @PostMapping("/upload/{patientEmail}/{doctorEmail}/{consultationId}")
    public ResponseEntity<String> handleDiagnosisFileUpload(@PathVariable String patientEmail,
            @PathVariable String doctorEmail, @PathVariable Long consultationId,
            @RequestParam("file") MultipartFile file) {

        System.err.println("UPLOAD FILE CALLED");

        try {
            medicalFileService.uploadDiagnosisFile(patientEmail, doctorEmail, file, consultationId);
            return ResponseEntity.ok("Diagnosis file uploaded successfully");
        } catch (Exception e) {  // ✅ Catching generic exception
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload diagnosis file");
        }
    }

    @GetMapping("/patients/{doctorEmail}")
    public ResponseEntity<List<String>> getPatientEmails(@PathVariable String doctorEmail) {
        try {
            List<String> patientEmails = medicalFileService.getRecipentEmailsList(doctorEmail);
            if (patientEmails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(patientEmails);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/files/{patientEmail}")
    public ResponseEntity<List<String>> getPatientFiles(@PathVariable String patientEmail) {
        try {
            List<String> patientFiles = medicalFileService.getFilePathsForPatient(patientEmail);
            if (patientFiles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(patientFiles);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/download/{patientEmail}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String patientEmail, @PathVariable String fileName) {
        try {
            Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                String contentType = "application/octet-stream"; // Default content type
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error downloading file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, HttpServletRequest request) {
        try {
            Resource resource = medicalFileService.loadFileAsResource(fileId);
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/consultation/{consultationId}")
    public List<MedicalFile> getFilesByConsultationId(@PathVariable Long consultationId) {
        return medicalFileService.getFilesByConsultationId(consultationId);
    }
}
