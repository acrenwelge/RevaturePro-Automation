package com.andrew.revpro.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public abstract class ExcelReader {
	
	
	/**
	 * Retrieves an "int" from a cell. 
	 * If the cell is numeric, the double value is cast to an int and returned. 
	 * If the cell is a string, the integer is parsed and returned. 
	 * @param cell
	 * @return
	 */
	public int getIntFromCell(Cell cell) {
		CellType ct = cell.getCellType();
		if (ct.equals(CellType.NUMERIC)) {
			return (int) cell.getNumericCellValue();
		} else if (ct.equals(CellType.STRING)) {
			return Integer.parseInt(cell.getStringCellValue());
		}
		return 0;
	}
}
