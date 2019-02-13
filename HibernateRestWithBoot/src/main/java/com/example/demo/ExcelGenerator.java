package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


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
	public static void excelReader(MultipartFile reapExcelDataFile) throws IOException {
		List<User> tempUserList = new ArrayList<User>();
	    XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
	    XSSFSheet worksheet = workbook.getSheetAt(0);

	    for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
	        User tempUser = new User();

	        XSSFRow row = worksheet.getRow(i);

	        tempUser.setId((int) row.getCell(0).getNumericCellValue());
	        tempUser.setFirstName(row.getCell(1).getStringCellValue());
	        tempUser.setLastName(row.getCell(2).getStringCellValue());
	        tempUser.setEmail(row.getCell(3).getStringCellValue());
	        tempUser.setPhoneNo(row.getCell(4).getStringCellValue());
	        tempUser.setRole(row.getCell(5).getStringCellValue());
	            tempUserList.add(tempUser);   
	    }
	    
	    
	    UserDao userDao = new UserDao();
	    List<User> existingUsers = userDao.getAllUsers();
	    
	    List<User> oldUsers = new ArrayList<User>();
	    
	    for(User user: tempUserList) {
	    	for(User existingUser: existingUsers) {
	    		if(user.getFirstName().equals(existingUser.getFirstName())||user.getLastName().equals(existingUser.getLastName())) {
	    		 oldUsers.add(user);
	    		 break;
	    		}
	    	}
	    }
	    
	    tempUserList.removeAll(oldUsers);
	    for(User newUser: tempUserList) {
	    	userDao.addUser(newUser);
	    }
		
	}

}
