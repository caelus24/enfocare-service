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

	@Autowired
	private MedicalFileRepository medicalFileRepository;

	@Autowired
	private MedicalFileMapper medicalFileMapper;

	@Value("${medicalfile.dir}")
	private String diagnosisDir;

	public List<String> getRecipentEmailsList(String doctorEmail) {

		return medicalFileRepository.findDistinctPatientEmailsByDoctorEmail(doctorEmail);
	}

	public void protectPdfFile(String paramFile) throws MalformedURLException {
		File file = new File(paramFile);
		System.err.println("FILENAME RAW : " + file.getAbsolutePath());
		System.err.println("FILENAMEs : " + paramFile);

		try {
			PDDocument document = Loader.loadPDF(file);

			AccessPermission ap = new AccessPermission();

			StandardProtectionPolicy spp = new StandardProtectionPolicy("123456", "123456", ap);

			spp.setEncryptionKeyLength(128);

			// Setting the access permissions
			spp.setPermissions(ap);

			// Protecting the document
			document.protect(spp);

			System.out.println("Document encrypted");

			// Create a temporary file to save the encrypted PDF
			File tempFile = File.createTempFile("temp_", ".pdf");

			// Saving the document to the temporary file
			document.save(tempFile.getAbsolutePath());

			// Closing the document
			document.close();

			// Rename the temporary file to replace the original file
			Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void uploadDiagnosisFile(String patientEmail, String doctorEmail, MultipartFile file, Long consultationId)
			throws IOException {

		System.err.println("FILENAME : " + file.getName());
		String modifiedEmail = patientEmail.replaceAll("@|\\.", "");
		String directoryPath = diagnosisDir + File.separator + modifiedEmail;
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		String originalFilename = file.getOriginalFilename();
		String filePath = Paths.get(directoryPath, originalFilename).toString();

		// Save the file locally
		Files.copy(file.getInputStream(), Path.of(filePath));

		// Generate a password for the file, assuming generatePassword() is implemented
		// elsewhere
		String password = generatePassword();

		// Create and save the MedicalFileEntity
		MedicalFileEntity medicalFileEntity = new MedicalFileEntity();
		medicalFileEntity.setPatientEmail(patientEmail);
		medicalFileEntity.setDoctorEmail(doctorEmail);
		medicalFileEntity.setFilePath(filePath);
		medicalFileEntity.setPassword(password);
		medicalFileEntity.setConsultationId(consultationId);

		medicalFileRepository.save(medicalFileEntity);
	}

	// Placeholder for the generatePassword method
	private String generatePassword() {
		// Implement password generation logic here
		return "generatedPassword";
	}

	public List<String> getFilePathsForPatient(String patientEmail) {
		return medicalFileRepository.findFilePathsByPatientEmail(patientEmail);
	}

	public Resource loadFileAsResource(Long fileId) throws MalformedURLException {
		Optional<MedicalFileEntity> fileEntityOptional = medicalFileRepository.findById(fileId);
		if (fileEntityOptional.isPresent()) {

			protectPdfFile(fileEntityOptional.get().getFilePath());
			Path filePath = Paths.get(fileEntityOptional.get().getFilePath()).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				// Handle the case where the file doesn't exist or is not accessible
				throw new MalformedURLException("File not found " + fileId);
			}
		} else {
			// Handle the case where the file entity was not found in the database
			throw new MalformedURLException("File entity not found " + fileId);
		}
	}

	public List<MedicalFile> getFilesByConsultationId(Long consultationId) {
		// TODO Auto-generated method stub
		return medicalFileRepository.findByConsultationId(consultationId).stream()
				.map(medicalFileEntity -> medicalFileMapper.map(medicalFileEntity)).collect(Collectors.toList());
	}

}
