package com.andrew.revpro.pom;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.andrew.revpro.model.Activity;
import com.andrew.revpro.model.CurriculumDay;
import com.andrew.revpro.model.CurriculumWeek;

public class CurriculumEditorPage {
	
	private static String tableXpath = "//*[@id=\"app-main\"]/app-view-curriculum/div[2]/div/div[2]/div/div";
	private WebDriver driver;
	private CurriculumEditorModal cem;
	
	public CurriculumEditorPage(WebDriver wd, CurriculumEditorModal cem) {
		this.driver = wd;
		this.cem = cem;
	}
	
	public WebElement getNewWeekBtn() {
		return driver.findElement(By.xpath("//*[@id=\"app-main\"]/app-view-curriculum/div[2]/div/div[3]/div/div/button"));
	}
	
	public void clickEditNthWeek(int weekNum) {
		//driver.findElement(By.xpath(tableXpath + "/div[2]/div/div[1]/span/a"));
		WebElement week = getNthWeek(weekNum);
		week.findElement(By.xpath("//a[@title='Edit']")).click();
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
		uiDay.click();
		for (Activity act : cDay.getActivities()) {
			cem.enterActivityTitle(act.getName());
			cem.saveActivity();
		}
	}
	
	private WebElement getNthWeek(int weekNum) {
		return driver.findElement(By.xpath(tableXpath + "/div[" + (weekNum+1) + "]/div")); // add 1 b/c first row is headers
	}
	
	private List<WebElement> getAllWeeks() {
		return driver.findElements(By.className("weeks"));
	}
}
