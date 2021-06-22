package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFHelper {
	private static final Logger logger = LoggerFactory.getLogger(PDFHelper.class);

	public static ByteArrayInputStream exportPDF(List<User> users) {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(60);
			table.setWidths(new int[] { 1, 3, 3 });

			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("Id", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("UserName", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Password", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("FirstName", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("LastName", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Role", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("CreateDate", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("LastModifiedDate", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("BirthDay", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("PhoneNumber", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Status", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Email", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Address", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Gender", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			for (User user : users) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Set<ERole> strRole = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(user.getId().toString()));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(user.getUserName()));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(user.getPassword()));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(user.getFirstName()));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(user.getLastName()));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(strRole)));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(formatter.format(user.getCreateDate())));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(formatter.format(user.getLastModifiedDate())));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(formatter.format(user.getBirthDay())));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(user.getPhoneNumber()));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(user.getStatus())));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(user.getEmail()));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(user.getAddress()));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(user.getGender())));
				table.addCell(cell);
			}

			PdfWriter.getInstance(document, out);
			document.open();
			document.add(table);

			document.close();

		} catch (DocumentException ex) {

			logger.error("Error occurred: {0}", ex);
		}

		return new ByteArrayInputStream(out.toByteArray());
	}
}
