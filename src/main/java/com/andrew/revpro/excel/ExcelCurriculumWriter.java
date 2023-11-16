package com.andrew.revpro.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.andrew.revpro.curriculum.data.Activity;
import com.andrew.revpro.curriculum.data.Curriculum;
import com.andrew.revpro.curriculum.data.CurriculumDay;
import com.andrew.revpro.curriculum.data.CurriculumWeek;

public class ExcelCurriculumWriter {
	private static final Logger log = LogManager.getLogger();
	private static final Path EXCEL_OUTPUT_LOCATION = Paths.get(System.getProperty("user.home"), "Documents", "extracted-revpro-curricula");
	private static File document;
	
	public static void writeQuizToNewExcelFile(Curriculum curr) throws IOException {
		Files.createDirectories(EXCEL_OUTPUT_LOCATION);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = dateFormat.format(new Date());
		if (curr.getName().isBlank()) {
			curr.setName("Exam-"+timestamp);
		}
		document = EXCEL_OUTPUT_LOCATION.resolve(FileUtil.sanitizeFileName(curr.getName()) + ".xlsx").toFile();
		try (// declare both resources here
				FileOutputStream fos = new FileOutputStream(document);
				Workbook wb = new XSSFWorkbook();
			) {
			Sheet sheet = wb.createSheet();
			int rowIdx = 0;
			int colIdx = 0;
			Row headerRow = sheet.createRow(rowIdx++);
			headerRow.createCell(colIdx++, CellType.STRING).setCellValue("Week");
			headerRow.createCell(colIdx++, CellType.STRING).setCellValue("MON");
			headerRow.createCell(colIdx++, CellType.STRING).setCellValue("TUE");
			headerRow.createCell(colIdx++, CellType.STRING).setCellValue("WED");
			headerRow.createCell(colIdx++, CellType.STRING).setCellValue("THU");
			headerRow.createCell(colIdx++, CellType.STRING).setCellValue("FRI");
			for (CurriculumWeek week : curr.getWeeks()) {
				// the maximum topics in a day for a given week defines the number of excel rows to create  
				int numRows = week.getDays().stream()
						.map(d -> d.getActivities().size())
						.max(Integer::compare)
						.orElse(2);
				List<CurriculumDay> days = week.getDays();
				List<List<Activity>> dayActivityList = days.stream().map(d->d.getActivities()).collect(Collectors.toList());
				for (int i=0; i < numRows; i++) {
					int cellInit = 0;
					Row r = sheet.createRow(rowIdx++);
					if (i == 0) {
						r.createCell(cellInit++, CellType.STRING).setCellValue(week.getName());
					} else if (i == 1) {
						r.createCell(cellInit++, CellType.STRING).setCellValue(week.getSubtitle());
					} else {
						r.createCell(cellInit++, CellType.STRING).setCellValue("");
					}
					for (int x=0; x < dayActivityList.size(); x++) {
						try {
							r.createCell(cellInit++, CellType.STRING).setCellValue(dayActivityList.get(x).get(i).getName());
						} catch (NullPointerException e) {
							// no more topics in this day of the week for this week
						}
					}
				}
			}
			wb.write(fos);
		}
		log.info("Excel quiz file saved: %s%n", document);
	}
	
}
