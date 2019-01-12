package com.andrew.revpro.pom;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CurriculaListPage {
	private WebDriver driver;
	
	public WebElement getCreateNewButton() {
		return driver.findElement(By.xpath("//*[@id=\"app-main\"]/app-dashboard/div[1]/div[1]/div/div[2]/button"));
	}
	
	/**
	 * Iterates through and returns the edit button associated with the first curriculum whose name matches the given string
	 * @param curriculumName - the curriculum to search for
	 * @return an {@link Optional} containing the button {@link WebElement} if a curriculum match is found,
	 * otherwise an empty {@link Optional} is returned
	 */
	public Optional<WebElement> getEditCurriculaBtnByName(String curriculumName) {
		String tbodyPath = "//*[@id=\"app-main\"]/app-dashboard/div[1]/div[3]/div[1]/table/tbody/";
		List<WebElement> curriculaNameList = driver.findElements(By.xpath(tbodyPath + "tr/th/div"));
		for (int i = 0; i < curriculaNameList.size(); i++) {
			String currentName = curriculaNameList.get(i).findElement(By.tagName("th")).getText();
			if (currentName.trim().equalsIgnoreCase(curriculumName)) {
				String btnPath = tbodyPath + "tr["+i+"]/td[8]/div/a[1]";
				return Optional.of(driver.findElement(By.xpath(tbodyPath + btnPath))); 
			}
		}
		return Optional.empty();
	}
}
