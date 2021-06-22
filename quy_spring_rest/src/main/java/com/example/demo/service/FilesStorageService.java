package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

	public void delete(String fileDelete);

	public ByteArrayInputStream loadXLSX();

	public ByteArrayInputStream loadCSV();

	public ByteArrayInputStream loadPDF();

	public void buildHeader(Row headerRow, String[] headers);

	public void saveFile(MultipartFile file, Long id) throws IOException;
}
