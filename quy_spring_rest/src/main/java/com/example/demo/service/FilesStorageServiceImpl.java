package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
	@Value("${upload.path}")
	private String path;

	private static final Logger logger = LoggerFactory.getLogger(FilesStorageServiceImpl.class);

	@Autowired
	UserRepository userRepo;

	@Override
	public void delete(String fileDelete) {
		try {
			Path deletePath = Paths.get(path + fileDelete);
			Files.delete(deletePath);
		} catch (Exception e) {
			logger.error("file not found");
		}
	}

	@Override
	public ByteArrayInputStream loadXLSX() {
		List<User> users = userRepo.findAll();

		return ExcelHelper.usersToExcel(users);
	}

	@Override
	public ByteArrayInputStream loadCSV() {
		List<User> users = userRepo.findAll();

		return CSVHelper.usersToCSV(users);
	}

	@Override
	public void buildHeader(Row headerRow, String[] headers) {
		for (int col = 0; col < headers.length; col++) {
			Cell cell = headerRow.createCell(col);
			cell.setCellValue(headers[col]);
		}
	}

	public void saveFile(MultipartFile multipartFile, Long id) throws IOException {
		Path uploadPath = Paths.get(path + id);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(multipartFile.getOriginalFilename());
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + multipartFile.getOriginalFilename(), ioe);
		}
	}

	@Override
	public ByteArrayInputStream loadPDF() {
		List<User> users = userRepo.findAll();

		return PDFHelper.exportPDF(users);
	}
}
