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
import com.enfocareservice.repository.UserRepository;
import com.enfocareservice.entity.UserEntity;

@Service
public class MedicalFileService {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicalFileService.class);

    @Autowired
    private MedicalFileRepository medicalFileRepository;

    @Autowired
    private MedicalFileMapper medicalFileMapper;

    @Autowired
    private UserRepository userRepository;

    @Value("${medicalfile.dir:/app/data/images}")
    private String diagnosisDir;

    public List<String> getRecipentEmailsList(String doctorEmail) {
        return medicalFileRepository.findDistinctPatientEmailsByDoctorEmail(doctorEmail);
    }

    public List<String> getFilePathsForPatient(String patientEmail) {
        return medicalFileRepository.findFilePathsByPatientEmail(patientEmail);
    }
    
    public Optional<MedicalFile> getFileByPatientAndFileName(String patientEmail, String fileName) {
        return medicalFileRepository.getFileByPatientAndFileName(patientEmail, fileName)
            .map(medicalFileMapper::map);
    }

    public Optional<MedicalFile> getFileById(Long fileId) {
        return medicalFileRepository.getFileById(fileId)
            .map(medicalFileMapper::map);
    }

    public List<MedicalFile> getFilesByConsultationId(Long consultationId) {
        logger.info("Fetching files for consultation ID: {}", consultationId);
        return medicalFileRepository.findByConsultationId(consultationId).stream()
                .map(medicalFileEntity -> {
                    MedicalFile file = medicalFileMapper.map(medicalFileEntity);
                    String baseUrl = "https://enfocare-service-production.up.railway.app/enfocare/medical-file/download/";
                    file.setFileUrl(baseUrl + file.getId());
                    return file;
                })
                .collect(Collectors.toList());
    }

    public List<MedicalFile> getFilesByConsultationAndDoctor(Long consultationId, String doctorEmail) {
        logger.info("Fetching files for Consultation ID: {} and Doctor: {}", consultationId, doctorEmail);
        return medicalFileRepository.findByConsultationIdAndDoctorEmail(consultationId, doctorEmail).stream()
                .map(medicalFileMapper::map)
                .collect(Collectors.toList());
    }
    
    public List<MedicalFile> getFilesByConsultationAndPatient(Long consultationId, String patientEmail) {
        logger.info("Fetching files for Consultation ID: {} and Patient: {}", consultationId, patientEmail);
        return medicalFileRepository.findByConsultationIdAndPatientEmail(consultationId, patientEmail).stream()
                .map(medicalFileMapper::map)
                .collect(Collectors.toList());
    }

    public void protectPdfFile(String paramFile, String password) {
        File file = new File(paramFile);
        logger.info("🔒 Encrypting file: {}", file.getAbsolutePath());

        try (PDDocument document = Loader.loadPDF(file)) {
            AccessPermission ap = new AccessPermission();
            StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
            spp.setEncryptionKeyLength(128);
            spp.setPermissions(ap);
            document.protect(spp);
            document.save(paramFile);
            logger.info("✅ Document encrypted successfully");
        } catch (IOException e) {
            logger.error("❌ Error encrypting PDF file: {}", paramFile, e);
        }
    }

    public void uploadDiagnosisFile(String patientEmail, String doctorEmail, MultipartFile file, Long consultationId) {
        try {
            logger.info("📤 Uploading file for Patient: {} by Doctor: {}", patientEmail, doctorEmail);
            
            if (file == null || file.isEmpty()) {
                logger.error("❌ No file received!");
                return;
            }

            String modifiedEmail = patientEmail.replaceAll("@|\\.", "");
            String directoryPath = diagnosisDir + File.separator + modifiedEmail;
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String filePath = Paths.get(directoryPath, originalFilename).toString();
            Files.copy(file.getInputStream(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);

            MedicalFileEntity medicalFileEntity = new MedicalFileEntity();
            medicalFileEntity.setPatientEmail(patientEmail);
            medicalFileEntity.setDoctorEmail(doctorEmail);
            medicalFileEntity.setFilePath(filePath);
            medicalFileEntity.setConsultationId(consultationId);
            medicalFileRepository.save(medicalFileEntity);

            logger.info("✅ File metadata saved in database!");
        } catch (IOException e) {
            logger.error("❌ Error uploading file for patient: {}", patientEmail, e);
        }
    }

    public Resource loadFileAsResource(Long fileId, String userEmail) throws IOException {
        try {
            Optional<MedicalFileEntity> fileEntityOptional = medicalFileRepository.findById(fileId);
            if (fileEntityOptional.isPresent()) {
                Path filePath = Paths.get(fileEntityOptional.get().getFilePath()).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists() && resource.isReadable()) {
                    logger.info("✅ Successfully loaded file: {}", filePath);
                    String password = generateUserPassword(userEmail);
                    protectPdfFile(filePath.toString(), password);
                    return resource;
                } else {
                    throw new IOException("File cannot be read: " + fileId);
                }
            } else {
                throw new IOException("File entity not found: " + fileId);
            }
        } catch (Exception e) {
            throw new IOException("Error loading file: " + fileId, e);
        }
    }

    private String generateUserPassword(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.map(u -> u.getLastName() + u.getBirthYear()).orElse("defaultPassword");
    }
}
