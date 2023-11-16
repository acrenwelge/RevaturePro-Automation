package com.andrew.revpro.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.andrew.revpro.curriculum.data.Activity;
import com.andrew.revpro.curriculum.data.ActivityType;
import com.andrew.revpro.curriculum.data.Curriculum;
import com.andrew.revpro.curriculum.data.CurriculumDay;
import com.andrew.revpro.curriculum.data.CurriculumWeek;

public class CurriculumEditorPage {
	private WebDriver driver;
	
	public CurriculumEditorPage(WebDriver wd) {
		this.driver = wd;
	}
	
	/*
	 * READ operations
	 */
	
	public Curriculum extractEntireCurriculum() {
		Curriculum curr = new Curriculum();
		List<WebElement> weeks = getAllWeeks();
		for (WebElement week : weeks) {
			CurriculumWeek weekData = extractWeekData(week);
			curr.addWeek(weekData);
		}
		return curr;
	}
	
	public CurriculumWeek extractWeekData(WebElement weekDiv) {
		CurriculumWeek data = new CurriculumWeek();
		List<WebElement> cellDivs = weekDiv.findElements(By.tagName("div"));
		String name = cellDivs.get(0).getText();
		String subtitle = cellDivs.get(0).findElement(By.tagName("div")).getText();
		data.setName(name);
		data.setSubtitle(subtitle);
		List<CurriculumDay> days = new ArrayList<>();
		cellDivs.remove(0); // week header data extracted already - ignore
		for (WebElement day : cellDivs) {
			CurriculumDay dayData = extractDayTopics(day);
			days.add(dayData);
		}
		return data;
	}
	
	public CurriculumDay extractDayTopics(WebElement dayDiv) {
		CurriculumDay day = new CurriculumDay();
		List<Activity> activities = new ArrayList<>();
		List<WebElement> topics = dayDiv.findElements(By.tagName("div"));
		topics.forEach(t -> {
			activities.add(new Activity(t.getText(),ActivityType.LECTURE));
		});
		day.setActivities(activities);
		return day;
	}
	
	/*
	 * WRITE operations
	 */
	
	public void createNewWeek() {
		driver.findElement(By.id("currVwAddWksBtn")).click();
	}
	
	public void enterWeekActivities(int weekNum, CurriculumWeek cWeek) {
		WebElement week = getNthWeek(weekNum);
		List<WebElement> dayElements = week.findElements(By.xpath("/div"));
		int dayNum = 0; // 0-based - for retrieving days from curriculum week object
		// the UI has days from div's #2-6
		for (int i = 2; i <= 6; i++) {
			WebElement dayElement = dayElements.get(i);
			enterDayActivities(cWeek.getDays().get(dayNum++), dayElement);
		}
	}
	
	public void enterDayActivities(CurriculumDay cDay, WebElement uiDay) {
		for (Activity act : cDay.getActivities()) {
			uiDay.click();
			// consult CurriculumEditorModal object
		}
	}
	
	/*
	 * Utility methods
	 */
	
	public void clickEditNthWeek(int weekNum) {
		WebElement week = getNthWeek(weekNum);
		week.findElement(By.xpath("//a[@title='Edit'")).click();
	}
	
	/**
	 * Returns the 1-indexed Nth week
	 * @param weekNum - week index (starting at 1)
	 * @return the div containing the entire week row
	 */
	private WebElement getNthWeek(int weekNum) {
		List<WebElement> weekEls = getAllWeeks();
		return weekEls.get(weekNum-1);
	}
	
	private List<WebElement> getAllWeeks() {
		return driver.findElements(By.className("weeks"));
	}
}
