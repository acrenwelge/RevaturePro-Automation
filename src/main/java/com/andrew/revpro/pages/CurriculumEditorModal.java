package com.andrew.revpro.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.andrew.revpro.curriculum.data.ActivityType;

public class CurriculumEditorModal {
	private WebDriver driver;
	
	public CurriculumEditorModal(WebDriver wd) {
		this.driver = wd;
	}
	
	//TODO: ADD FUNCTIONALITY FOR PROJECT SCHEDULING AND ADDING PROJECT SKILLS
	
	public void enterActivityTitle(String title) {
		// only works for 1st item added...
		driver.findElement(By.id("form1")).sendKeys(title);
		// alternatively...
		List<WebElement> list = driver.findElements(By.id("form1"));
		for (WebElement we : list) {
			we.sendKeys(title); // pass in a list / array of strings instead and enter here
		}
	}
	
	public void selectActivity(ActivityType act) {
		// only first three buttons are visible initially
		String actBtnFirstThree = "//*[@id=\"frameModalTop\"]/div/div/div[2]/div[2]/div/div[1]";
		String dropdownPath = "//*[@id=\"frameModalTop\"]/div/div/div[2]/div[2]/div/div[2]/div/div";
		// alternatively, search by class "dropdown-item"
		if (act.equals(ActivityType.LECTURE)) {
			driver.findElement(By.xpath(actBtnFirstThree + "/span[1]/span")).click();
		} else if (act.equals(ActivityType.PROJECT)) {
			driver.findElement(By.xpath(actBtnFirstThree + "/span[2]/span")).click();
		} else if (act.equals(ActivityType.QUIZ)) {
			driver.findElement(By.xpath(actBtnFirstThree + "/span[3]/span")).click();
		} else if (act.equals(ActivityType.INTERVIEW)) {
			//*[@id="frameModalTop"]/div/div/div[2]/div[2]/div/div[2]/div/div/a[4]/span
			driver.findElement(By.xpath(dropdownPath + "/a[4]/span"));
		} else if (act.equals(ActivityType.MILESTONE)) {
			driver.findElement(By.xpath(dropdownPath + "/a[5]/span"));
		} else if (act.equals(ActivityType.VIDEO)) {
			driver.findElement(By.xpath(dropdownPath + "/a[6]/span"));
		} else if (act.equals(ActivityType.REFERENCE)) {
			driver.findElement(By.xpath(dropdownPath + "/a[7]/span"));
		} else if (act.equals(ActivityType.QUALITY_AUDIT)) {
			driver.findElement(By.xpath(dropdownPath + "/a[8]/span"));
		}
	}
	
	public void clickAddNewActivity() {
		// this may only work the first time, since the DOM changes when an activity is added
		driver.findElement(By.xpath("//*[@id=\"frameModalTop\"]/div/div/div[2]/div[1]/div/i")).click();
		// alternatively...
		driver.findElement(By.id("form1")).sendKeys(Keys.ENTER); // as long as the "+" icon as been clicked once already
	}
	
	public void saveActivity() {
		driver.findElement(By.xpath("//*[@id=\"frameModalTop\"]/div/div/div[3]/button"));
	}
	
	public void cancelActivity() {
		driver.findElement(By.xpath("//*[@id=\"frameModalTop\"]/div/div/div[1]/button/span"));
	}
}
