package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;

public class ExcelHelper {

	@Autowired
	static FilesStorageServiceImpl storageService;

	private static final String[] HEADERS = { "Id", "UserName", "Password", "FirstName", "LastName", "Role",
			"CreateDate", "LastModifiedDate", "BirthDay", "PhoneNumber", "Status", "Email", "Address", "Gender" };
	private static final String SHEET = "Users";

	public static ByteArrayInputStream usersToExcel(List<User> users) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			Sheet sheets = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheets.createRow(0);
//			storageService.buildHeader(headerRow, HEADERS);
			for (int col = 0; col < HEADERS.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERS[col]);
			}

			int rowIdx = 1;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			for (User user : users) {
				Row row = sheets.createRow(rowIdx++);
				Set<ERole> strRole = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

				row.createCell(0).setCellValue(user.getId());
				row.createCell(1).setCellValue(user.getUserName());
				row.createCell(2).setCellValue(user.getPassword());
				row.createCell(3).setCellValue(user.getFirstName());
				row.createCell(4).setCellValue(user.getLastName());
				row.createCell(5).setCellValue(String.valueOf(strRole));
				row.createCell(6).setCellValue(formatter.format(user.getCreateDate()));
				row.createCell(7).setCellValue(formatter.format(user.getLastModifiedDate()));
				row.createCell(8).setCellValue(formatter.format(user.getBirthDay()));
				row.createCell(9).setCellValue(user.getPhoneNumber());
				row.createCell(10).setCellValue(String.valueOf(user.getStatus()));
				row.createCell(11).setCellValue(user.getEmail());
				row.createCell(12).setCellValue(user.getAddress());
				row.createCell(13).setCellValue(String.valueOf(user.getGender()));
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}
}
