package com.enfocareservice.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enfocareservice.entity.MedicalFileEntity;
import com.enfocareservice.model.MedicalFile;
import com.enfocareservice.model.mapper.MedicalFileMapper;
import com.enfocareservice.repository.MedicalFileRepository;

@Service
public class MedicalFileService {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicalFileService.class);

    @Autowired
    private MedicalFileRepository medicalFileRepository;

    @Autowired
    private MedicalFileMapper medicalFileMapper;

    @Value("${medicalfile.dir}")
    private String diagnosisDir;

    public List<String> getRecipentEmailsList(String doctorEmail) {
        return medicalFileRepository.findDistinctPatientEmailsByDoctorEmail(doctorEmail);
    }

    public void protectPdfFile(String paramFile) {
        File file = new File(paramFile);
        logger.info("Processing file encryption: {} TUNAMAYO", file.getAbsolutePath());

        try {
            PDDocument document = Loader.loadPDF(file);
            AccessPermission ap = new AccessPermission();
            StandardProtectionPolicy spp = new StandardProtectionPolicy("123456", "123456", ap);
            spp.setEncryptionKeyLength(128);
            spp.setPermissions(ap);
            document.protect(spp);
            logger.info("Document encrypted successfully. TUNAMAYO");

            File tempFile = File.createTempFile("temp_", ".pdf");
            document.save(tempFile.getAbsolutePath());
            document.close();
            Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Error encrypting PDF file: {} TUNAMAYO", paramFile, e);
        }
    }

    public void uploadDiagnosisFile(String patientEmail, String doctorEmail, MultipartFile file, Long consultationId) {
        try {
            logger.info("Uploading diagnosis file for patient: {} by doctor: {}", patientEmail, doctorEmail);
            String modifiedEmail = patientEmail.replaceAll("@|\\.", "");
            String directoryPath = diagnosisDir + File.separator + modifiedEmail;
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String filePath = Paths.get(directoryPath, originalFilename).toString();

            Files.copy(file.getInputStream(), Path.of(filePath));
            String password = generatePassword();

            MedicalFileEntity medicalFileEntity = new MedicalFileEntity();
            medicalFileEntity.setPatientEmail(patientEmail);
            medicalFileEntity.setDoctorEmail(doctorEmail);
            medicalFileEntity.setFilePath(filePath);
            medicalFileEntity.setPassword(password);
            medicalFileEntity.setConsultationId(consultationId);

            medicalFileRepository.save(medicalFileEntity);
            logger.info("File uploaded successfully: {}", filePath);
        } catch (IOException e) {
            logger.error("Error uploading file for patient: {} TUNAMAYO", patientEmail, e);
        }
    }

    private String generatePassword() {
        return "generatedPassword";
    }

    public List<String> getFilePathsForPatient(String patientEmail) {
        return medicalFileRepository.findFilePathsByPatientEmail(patientEmail);
    }

    public Resource loadFileAsResource(Long fileId) throws MalformedURLException {
        try {
            Optional<MedicalFileEntity> fileEntityOptional = medicalFileRepository.findById(fileId);
            if (fileEntityOptional.isPresent()) {
                protectPdfFile(fileEntityOptional.get().getFilePath());
                Path filePath = Paths.get(fileEntityOptional.get().getFilePath()).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists()) {
                    return resource;
                } else {
                    logger.error("File not found: {} TUNAMAYO", fileId);
                    throw new MalformedURLException("File not found " + fileId);
                }
            } else {
                logger.error("File entity not found for ID: {} TUNAMAYO", fileId);
                throw new MalformedURLException("File entity not found " + fileId);
            }
        } catch (Exception e) {
            logger.error("Error loading file as resource for ID: {} TUNAMAYO", fileId, e);
            throw new MalformedURLException("Error loading file " + fileId);
        }
    }

    public List<MedicalFile> getFilesByConsultationId(Long consultationId) {
        logger.info("Fetching files for consultation ID: {}", consultationId);
        return medicalFileRepository.findByConsultationId(consultationId).stream()
                .map(medicalFileEntity -> medicalFileMapper.map(medicalFileEntity))
                .collect(Collectors.toList());
    }
}
