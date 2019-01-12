package com.andrew.revpro.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.andrew.revpro.exceptions.ExcelException;

public class ExcelExperimentation {
	
	private static final String EXCEL_INPUT_FILE = "C:/Users/Revature/Desktop/testinput.xlsx";
	private static final String EXCEL_OUTPUT_FILE = "C:/Users/Revature/Desktop/testoutput.xlsx";
	
	// inner types
	enum Gender {
		MALE, FEMALE
	}
	
	class Person {
	    private String firstName;
	    private String lastName;
	    private int age;
	    private Gender gender;
	 
	    public Person(String firstName, String lastName, int age, Gender gender) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
			this.age = age;
			this.gender = gender;
		}
	    
	    public Person() {
	    	super();
	    }

		public String toString() {
	        return String.format("%s %s - %s - %d", firstName, lastName, gender, age);
	    }

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public Gender getGender() {
			return gender;
		}

		public void setGender(Gender gender) {
			this.gender = gender;
		}
		public void setGender(String which) {
			if (which.equalsIgnoreCase("M"))
				this.gender = Gender.MALE;
			else
				this.gender = Gender.FEMALE;
		}
	}
	
	public static void main(String[] args) {
		try {
			//new ExcelExperimentation().readExcelFile();
			//new ExcelExperimentation().writeToNewExcelFile();
			new ExcelExperimentation().readAndWriteNewExcelFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates data for other methods
	 * @return a list of Persons
	 */
	public List<Person> getListOfPeople() {
		List<Person> list = new ArrayList<>();
		list.add(new Person("William","Gentry",24,Gender.MALE));
		list.add(new Person("Carolyn","Rehm",24,Gender.FEMALE));
		list.add(new Person("Karan","Dhirar",32,Gender.MALE));
		return list;
	}

	/**
	 * Reads a list of Persons from an excel file, iterating over the defined rows 
	 * and the cells defined within those rows.
	 * @throws ExcelException
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	public List<Person> readExcelFile() throws ExcelException, IOException, InvalidFormatException {
        try (
        		FileInputStream inputStream = new FileInputStream(new File(EXCEL_INPUT_FILE));
        		Workbook workbook = WorkbookFactory.create(new File(EXCEL_INPUT_FILE));
        		) {
        	Sheet firstSheet = workbook.getSheetAt(0);
        	Iterator<Row> rowIter = firstSheet.iterator();
        	List<Person> listPpl = new ArrayList<>();
        	rowIter.next(); // skip the first row (headers)
        	while (rowIter.hasNext()) {
        		Row nextRow = rowIter.next();
        		Person p = new ExcelExperimentation().new Person();
        		Iterator<Cell> cellIter = nextRow.cellIterator();
        		while (cellIter.hasNext()) {
        			Cell currentCell = cellIter.next();
        			int colIdx = currentCell.getColumnIndex();
        			switch (colIdx) {
        			case 0: p.setFirstName(currentCell.getStringCellValue()); break;
        			case 1: p.setLastName(currentCell.getStringCellValue()); break;
        			case 2: p.setAge(getIntFromCell(currentCell)); break;
        			case 3: p.setGender(currentCell.getStringCellValue()); break;
        			default: throw new ExcelException("column index out of bounds");
        			}
        		}
        		listPpl.add(p);
        	}
        	return listPpl;
        }
	}
	
	/**
	 * Reads data from existing file, appends more data, and writes to a new excel file
	 * @throws IOException
	 * @throws ExcelException 
	 * @throws InvalidFormatException 
	 */
	public void readAndWriteNewExcelFile() throws IOException, ExcelException, InvalidFormatException {
		List<Person> listFromExcel = readExcelFile();
		List<Person> listToWrite = getListOfPeople();
		// compose cumulative list from Excel + generated data
		List<Person> cumulativeList = listFromExcel;
		cumulativeList.addAll(listToWrite);
		try (Workbook wb = new XSSFWorkbook()) {
			// grab first sheet
			Sheet sh = wb.createSheet();
			int rowInit = 0;
			for (Person p : cumulativeList) {
				int cellInit = 0;
				Row r = sh.createRow(rowInit++);
				Cell cell = r.createCell(cellInit++, CellType.STRING);
				cell.setCellValue(p.getFirstName());
				cell = r.createCell(cellInit++, CellType.STRING);
				cell.setCellValue(p.getLastName());
				cell = r.createCell(cellInit++, CellType.NUMERIC);
				cell.setCellValue(p.getAge());
				cell = r.createCell(cellInit++, CellType.STRING);
				String gender = p.getGender().equals(Gender.MALE) ? "M" : "F";
				cell.setCellValue(gender);
			}
			try (FileOutputStream fos = new FileOutputStream(new File(EXCEL_OUTPUT_FILE))) {
				wb.write(fos);
			}
		}
		System.out.printf("Excel file %s written successfully", EXCEL_OUTPUT_FILE);
	}
	
	/**
	 * Writes data to a new Excel file
	 * @throws IOException
	 */
	public void writeNewExcelFile() throws IOException {
		List<Person> listToWrite = getListOfPeople();
		try (// declare both resources here
				FileOutputStream fos = new FileOutputStream(new File(EXCEL_OUTPUT_FILE));
				Workbook wb = new XSSFWorkbook();
				) {
			// create new sheet
			Sheet sh = wb.createSheet();
			int rowInit = 0;
			for (Person p : listToWrite) {
				int cellInit = 0;
				Row r = sh.createRow(rowInit++);
				Cell cell = r.createCell(cellInit++, CellType.STRING);
				cell.setCellValue(p.getFirstName());
				cell = r.createCell(cellInit++, CellType.STRING);
				cell.setCellValue(p.getLastName());
				cell = r.createCell(cellInit++, CellType.NUMERIC);
				cell.setCellValue(p.getAge());
				cell = r.createCell(cellInit++, CellType.STRING);
				String gender = p.getGender().equals(Gender.MALE) ? "M" : "F";
				cell.setCellValue(gender);
			}
			wb.write(fos);
		}
		System.out.printf("Excel file %s written successfully", EXCEL_OUTPUT_FILE);
	}
	
	/**
	 * Retrieves an "int" from a cell. 
	 * If the cell is numeric, the double value is cast to an int and returned. 
	 * If the cell is a string, the integer is parsed and returned. 
	 * @param cell
	 * @return
	 */
	private int getIntFromCell(Cell cell) {
		CellType ct = cell.getCellType();
		if (ct.equals(CellType.NUMERIC)) {
			return (int) cell.getNumericCellValue();
		} else if (ct.equals(CellType.STRING)) {
			return Integer.parseInt(cell.getStringCellValue());
		}
		return 0;
	}

}
