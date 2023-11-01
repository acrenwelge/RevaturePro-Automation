package com.andrew.revpro.pom;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.andrew.revpro.model.curriculum.CurriculumType;
import com.andrew.revpro.model.curriculum.ProgramType;

public class NewCurriculumPage {
	private WebDriver driver;
	
	public NewCurriculumPage(WebDriver wd) {
		this.driver = wd;
	}
	
	public WebElement getCurriculumNameField() {
		return driver.findElement(By.id("curriculumName"));
	}
	
	public void selectDefaultSchedule() {
		// Monday-Friday corresponds to buttons 2-6
		for (int i=2; i<=6; i++) {
			driver.findElement(By.xpath("//*[@id=\"app-main\"]/app-create-curriculum/div/div/div[4]/div/div/div/button["+i+"]")).click();
		}
	}
	
	public void selectCurriculumType(CurriculumType ct) {
		// click the dropdown
		driver.findElement(By.xpath("//*[@id=\"app-main\"]/app-create-curriculum/div/div/div[6]/div/div/div/div/button")).click();
		String text = "";
		switch (ct) {
		case STANDARD:    text = "Standard";    break;
		case SPECIALIZED: text = "Specialized"; break;
		case CUSTOM:      text = "Custom";      break;
		}
		findAnchorTagByText(text).click();
	}
	
	private WebElement findAnchorTagByText(String text) {
		return driver.findElement(By.linkText(text));
	}
	
	public void enterTags(List<String> tags) {
		WebElement tagInput = driver.findElement(By.xpath("//*[@id=\"tag\"]/div/input"));
		for (String tag : tags) {
			tagInput.sendKeys(tag);
			tagInput.sendKeys(Keys.ENTER);
		}
	}
	
	public void selectProgramType(ProgramType pt) {
		// click the dropdown
		driver.findElement(By.xpath("//*[@id=\"app-main\"]/app-create-curriculum/div/div/div[8]/div/div/div/div/button")).click();
		String text = "";
		switch (pt) {
		case REGULAR: text = "Regular"; break;
		case SPARK_ONLINE: text = "Spark Online"; break;
		case SPARK_RESIDENTIAL: text = "Spark Residential"; break;
		}
		findAnchorTagByText(text).click();
	}
	
	public void clickNext() {
		//driver.findElement(By.xpath("//*[@id=\"app-main\"]/app-create-curriculum/div/div/div[9]/div/div/button[2]")).click();
		findAnchorTagByText("Next").click();
	}
}
