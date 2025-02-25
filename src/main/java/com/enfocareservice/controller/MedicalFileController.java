package com.enfocareservice.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.enfocareservice.model.MedicalFile;
import com.enfocareservice.service.MedicalFileService;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/enfocare/medical-file")
public class MedicalFileController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalFileController.class);
    
    @Autowired
    private MedicalFileService medicalFileService;

    @PostMapping("/upload/{patientEmail}/{doctorEmail}/{consultationId}")
    public ResponseEntity<String> handleDiagnosisFileUpload(
            @PathVariable String patientEmail,
            @PathVariable String doctorEmail, 
            @PathVariable Long consultationId,
            @RequestParam("file") MultipartFile file) {

        logger.info("Uploading file: {} for Patient: {} by Doctor: {}", file.getOriginalFilename(), patientEmail, doctorEmail);
        
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty or missing");
        }
        
        try {
            medicalFileService.uploadDiagnosisFile(patientEmail, doctorEmail, file, consultationId);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            logger.error("Failed to upload file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    @GetMapping("/patients/{doctorEmail}")
    public ResponseEntity<List<String>> getPatientEmails(@PathVariable String doctorEmail) {
        List<String> patientEmails = medicalFileService.getRecipentEmailsList(doctorEmail);
        return patientEmails.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(patientEmails);
    }
    
    @GetMapping("/files/{patientEmail}")
    public ResponseEntity<List<String>> getPatientFiles(@PathVariable String patientEmail) {
        List<String> patientFiles = medicalFileService.getFilePathsForPatient(patientEmail);
        return patientFiles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(patientFiles);
    }

    @GetMapping("/download/{patientEmail}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFileByPatient(@PathVariable String patientEmail, @PathVariable String fileName) {
        try {
            Optional<MedicalFile> fileOptional = medicalFileService.getFileByPatientAndFileName(patientEmail, fileName);
            if (fileOptional.isEmpty()) {
                logger.error("No file found for Patient: {} with name: {}", patientEmail, fileName);
                return ResponseEntity.notFound().build();
            }

            MedicalFile file = fileOptional.get();
            Path filePath = Paths.get(file.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                logger.error("File not found at path: {}", filePath);
                return ResponseEntity.notFound().build();
            }

            logger.info("Downloading file: {} for Patient: {}", fileName, patientEmail);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Error downloading file: {} for Patient: {}", fileName, patientEmail, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, HttpServletRequest request) {
        try {
            Resource resource = medicalFileService.loadFileAsResource(fileId, "user@example.com"); // Adjust email as needed
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Failed to download file with ID: {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/consultation/{consultationId}/doctor/{doctorEmail}")
    public ResponseEntity<List<MedicalFile>> getFilesByConsultationAndDoctor(
            @PathVariable Long consultationId, @PathVariable String doctorEmail) {
        List<MedicalFile> files = medicalFileService.getFilesByConsultationAndDoctor(consultationId, doctorEmail);
        return files.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(files);
    }
    
    @GetMapping("/consultation/{consultationId}/patient/{patientEmail}")
    public ResponseEntity<List<MedicalFile>> getFilesByConsultationAndPatient(
            @PathVariable Long consultationId, @PathVariable String patientEmail) {
        List<MedicalFile> files = medicalFileService.getFilesByConsultationAndPatient(consultationId, patientEmail);
        return files.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(files);
    }
}
