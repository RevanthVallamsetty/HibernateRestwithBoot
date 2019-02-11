package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelGenerator {
	public static ByteArrayInputStream usersToExcel(List<User> users) throws IOException {
		String[] COLUMNs = {"id", "firstName","lastName","email","phoneNo", "role"};
		try(
				Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
		){
			CreationHelper createHelper = workbook.getCreationHelper();
	 
			Sheet sheet = workbook.createSheet("Users");
	 
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());
	 
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
	 
			// Row for Header
			Row headerRow = sheet.createRow(0);
	 
			// Header
			for (int col = 0; col < COLUMNs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(COLUMNs[col]);
				cell.setCellStyle(headerCellStyle);
			}
	 
			// CellStyle for Age
			CellStyle ageCellStyle = workbook.createCellStyle();
			ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
	 
			int rowIdx = 1;
			for (User user : users) {
				Row row = sheet.createRow(rowIdx++);
	 
				row.createCell(0).setCellValue(user.getId());
				row.createCell(1).setCellValue(user.getFirstName());
				row.createCell(2).setCellValue(user.getLastName());
				row.createCell(3).setCellValue(user.getEmail());
				row.createCell(4).setCellValue(user.getPhoneNo());
				row.createCell(5).setCellValue(user.getRole());
				
			}
	 
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

}
