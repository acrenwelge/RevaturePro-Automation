package com.andrew.revpro.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import com.andrew.revpro.Util;
import com.andrew.revpro.model.Curriculum;
import com.andrew.revpro.model.CurriculumDay;
import com.andrew.revpro.model.CurriculumType;
import com.andrew.revpro.model.CurriculumWeek;
import com.andrew.revpro.model.ProgramType;

/**
 * Class for reading a curriculum defined in an excel file.
 * 
 * Excel files MUST have the week headings defined in the SAME ROW as week activities (basically,
 * Week 1 cannot be merged to be on the same line as the Monday, Tuesday, etc.. headings).
 * 
 * Also, this class checks each row that begins with "Week" in the first cell somewhere.
 * 
 * Default curriculum type is SPECIALIZED, default program type is REGULAR, and the
 * curriculum name is set as the name of the file.
 */
public class CurriculumReader extends ExcelReader {
	
	public Curriculum getCurriculum(File file) {
		Curriculum c = new Curriculum();
		// set other values here...
		try (Workbook w = WorkbookFactory.create(file)) {
			Sheet s = w.getSheetAt(0);
			c.setCurrType(CurriculumType.SPECIALIZED);
			c.setName(file.getName());
			c.setProgType(ProgramType.REGULAR);
			c.setWeeks(getWeeks(s));
		} catch(IOException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public List<CurriculumWeek> getWeeks(Sheet s) {
		List<CurriculumWeek> list = new ArrayList<>();
		Iterator<Row> it = s.rowIterator();
		while(it.hasNext()) {
			Row row = it.next();
			if (row.getCell(0) != null && 
				!row.getCell(0).getStringCellValue().trim().contains("Week")) continue;
			list.add(getWeek(row));
		}
		return list;
	}
	
	private CurriculumWeek getWeek(Row row) {
		CurriculumWeek wk = new CurriculumWeek();
		List<CurriculumDay> days = new ArrayList<>();
		for (int col = 0; col < 6; col++) {
			Cell cell = row.getCell(col, MissingCellPolicy.CREATE_NULL_AS_BLANK); // prevent NullPointerException with this policy
			String value = cell.getStringCellValue();
			System.out.println("Row: "+cell.getRowIndex() +", column: "+ cell.getColumnIndex());
			System.out.println(value);
			if (col == 0) { // Week definition cell
				String[] arr = value.split(":");
				wk.setName(arr[0]);
				if (arr.length > 1) {
					wk.setSubtitle(arr[1]);
				}
			} else if (col < 5) { // Day of week containing activity
				CurriculumDay day = new CurriculumDay();
				day.setActivities(Util.extractActivitiesFromBulletPoints(value));
				days.add(day);
			} else { // Environment
				wk.setEnvironments(Arrays.asList(Util.removeBulletPoints(value).split("\n")));
			}
		}
		return wk;
	}
	
}
