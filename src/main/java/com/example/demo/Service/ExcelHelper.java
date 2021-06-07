package com.example.demo.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.entity.User;

public class ExcelHelper {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "Id", "UserName", "Password", "FirstName", "LastName", "Role", "CreateDate", "LastModifiedDate", "BirthDay", "PhoneNumber", "Status", "Email", "Address", "dGender", "AvatarPath" };
	  static String SHEET = "Users";
	  
	  public static ByteArrayInputStream usersToExcel(List<User> users) {
		  try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	      Sheet sheet = workbook.createSheet(SHEET);
	      
	      // Header
	      Row headerRow = sheet.createRow(0);

	      for (int col = 0; col < HEADERs.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(HEADERs[col]);
	      }

	      int rowIdx = 1;
	      System.out.println("ba noi may");
	      for (User user : users) {
	        Row row = sheet.createRow(rowIdx++);

	        row.createCell(0).setCellValue(user.getId());
	        row.createCell(1).setCellValue(user.getUserName());
	        row.createCell(2).setCellValue(user.getPassword());
	        row.createCell(3).setCellValue(user.getFirstName());
	        row.createCell(4).setCellValue(user.getLastName());
//	        row.createCell(5).setCellValue(user.getRoles().toString());
	        row.createCell(5).setCellValue("alo");
	        row.createCell(6).setCellValue(user.getCreateDate());
	        row.createCell(7).setCellValue(user.getLastModiffiedDate());
	        row.createCell(8).setCellValue(user.getBirthDay());
	        row.createCell(9).setCellValue(user.getPhoneNumber().toString());
//	        row.createCell(10).setCellValue(user.getStatus().toString());
	        row.createCell(10).setCellValue("alo");
	        row.createCell(11).setCellValue(user.getEmail());
	        row.createCell(12).setCellValue(user.getAddress());
//	        row.createCell(13).setCellValue(user.getGender().toString());
	        row.createCell(13).setCellValue("alo");
	        row.createCell(14).setCellValue(user.getAvatarPath());
	      }
	      
	      workbook.write(out);
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	    	System.out.println("troi oi");
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	  }
}
