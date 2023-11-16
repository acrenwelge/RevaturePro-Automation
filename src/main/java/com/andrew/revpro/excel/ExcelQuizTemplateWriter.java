package com.andrew.revpro.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.andrew.revpro.quiz.data.Question;
import com.andrew.revpro.quiz.data.Quiz;

public class ExcelQuizTemplateWriter {
	
	private static final Logger log = LogManager.getLogger();
	private static final Path EXCEL_OUTPUT_LOCATION = Paths.get(System.getProperty("user.home"), "Documents", "imocha-uploads","extracted-revpro-exams");
	private static File document;
	
	public static void writeQuizToNewExcelFile(Quiz quiz) throws IOException {
		Files.createDirectories(EXCEL_OUTPUT_LOCATION);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = dateFormat.format(new Date());
		if (quiz.quizName.isBlank()) {
			quiz.quizName = "Exam-"+timestamp;
		}
		document = EXCEL_OUTPUT_LOCATION.resolve(FileUtil.sanitizeFileName(quiz.quizName) + ".xlsx").toFile();
		try (// declare both resources here
				FileOutputStream fos = new FileOutputStream(document);
				Workbook wb = new XSSFWorkbook();
			) {
			Sheet sh = wb.createSheet();
			int rowInit = 0;
			int col = 0;
			Row headerRow = sh.createRow(rowInit++);
			headerRow.createCell(col++, CellType.STRING).setCellValue("Question Type");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Difficulty Level");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Question Text");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Option (A)");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Option (B)");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Option (C)");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Option (D)");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Option (E)");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Correct Answer");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Answer Explanation");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Score");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Topics");
			headerRow.createCell(col++, CellType.STRING).setCellValue("Author");
			for (Question q : quiz.questions) {
				int cellInit = 0;
				Row r = sh.createRow(rowInit++);
				String quesTypeVal = null;
				switch (q.type) {
				case BEST_CHOICE: quesTypeVal = "MCQ";
				case MULTI_CHOICE: quesTypeVal = "MAQ";
				case TRUE_FALSE: quesTypeVal = "True/False";
				default: quesTypeVal = "MCQ";
				}
				r.createCell(cellInit++, CellType.STRING).setCellValue(quesTypeVal);
				r.createCell(cellInit++, CellType.STRING).setCellValue("Easy");
				r.createCell(cellInit++, CellType.STRING).setCellValue(q.questionText);
				r.createCell(cellInit++, CellType.STRING).setCellValue(q.ansA);
				r.createCell(cellInit++, CellType.STRING).setCellValue(q.ansB);
				r.createCell(cellInit++, CellType.STRING).setCellValue(q.ansC);
				r.createCell(cellInit++, CellType.STRING).setCellValue(q.ansD);
				r.createCell(cellInit++, CellType.STRING).setCellValue(q.ansE);
				r.createCell(cellInit++, CellType.STRING).setCellValue(q.correctAns);
				r.createCell(cellInit++, CellType.STRING).setCellValue("");
				r.createCell(cellInit++, CellType.NUMERIC).setCellValue(1);
				r.createCell(cellInit++, CellType.STRING).setCellValue("");
				r.createCell(cellInit++, CellType.STRING).setCellValue("");
			}
			wb.write(fos);
		}
		log.info("Excel quiz file saved: %s%n", document);
	}
	
}
