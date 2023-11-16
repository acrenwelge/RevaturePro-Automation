package com.andrew.revpro.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NewCurriculumPage {
	private WebDriver driver;
	
	public NewCurriculumPage(WebDriver wd) {
		this.driver = wd;
	}
	
	public void setCurriculumNameField(String name) {
		driver.findElement(By.id("crtCurrName")).sendKeys(name);
	}
	
	public void selectDefaultSchedule() {
		// Monday-Friday corresponds to buttons 2-6
		for (int i=2; i<=6; i++) {
			driver.findElement(By.xpath("//*[@id=\"app-main\"]/app-create-curriculum/div/div/div[4]/div/div/div/button["+i+"]")).click();
		}
	}
	
	public void toggleShowDescription() {
		driver.findElement(By.className("show-desc-switch")).findElement(By.className("lever")).click();
	}
	
	public void enterTags(List<String> tags) {
		WebElement tagInput = driver.findElement(By.id("crtCurrTag"));
		for (String tag : tags) {
			tagInput.sendKeys(tag);
			tagInput.sendKeys(Keys.ENTER);
		}
	}
	
	public void selectCurriculumType(String programType) {
		// click the dropdown
		driver.findElement(By.id("crtCurrPrgmTypeSel")).click();
		driver.findElement(By.linkText(programType)).click();
	}
	
	public void setClientName(String client) {
		driver.findElement(By.id("clientName")).sendKeys(client);
	}
	
	public void setMainTechnology(String tech) {
		driver.findElement(By.id("mainTechnology")).sendKeys(tech);
	}
	
	public void setSubTechnology(String tech) {
		driver.findElement(By.id("subTechnology")).sendKeys(tech);
	}
	
	public void setCompetency(String competencyName) {
		driver.findElement(By.id("competency")).sendKeys(competencyName);
	}
	
	public void clickNext() {
		driver.findElement(By.id("crtCurrNxtBtn")).click();
	}
	
	public void cancel() {
		driver.findElement(By.id("crtCurrCancelBtn")).click();
	}
}
