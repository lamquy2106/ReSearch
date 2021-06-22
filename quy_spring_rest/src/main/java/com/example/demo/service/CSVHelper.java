package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;

public class CSVHelper {
	public static ByteArrayInputStream usersToCSV(List<User> users) {
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			for (User user : users) {
				Set<ERole> strRole = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
				List<String> data = Arrays.asList(String.valueOf(user.getId()), user.getUserName(), user.getPassword(),
						user.getFirstName(), user.getLastName(), String.valueOf(strRole),
						formatter.format(user.getCreateDate()), formatter.format(user.getLastModifiedDate()),
						formatter.format(user.getBirthDay()), user.getPhoneNumber(), String.valueOf(user.getStatus()),
						user.getEmail(), user.getAddress(), String.valueOf(user.getGender()));

				csvPrinter.printRecord(data);
			}

			csvPrinter.flush();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
		}
	}
}
