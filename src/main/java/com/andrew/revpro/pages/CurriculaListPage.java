package com.andrew.revpro.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CurriculaListPage {
	private WebDriver driver;
	private WebDriverWait wait;
	private static final String URL = "https://app.revature.com/admin-v2/curriculum/dashboard";
	private static final Logger log = LogManager.getLogger();
	
	public CurriculaListPage(WebDriver wd) {
		this.driver = wd;
		this.wait = new WebDriverWait(driver, 10);
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(id="searchtext")
	WebElement searchbox;
	
	@FindBy(id="curDashTbl")
	WebElement curriculaList;
	
	public void navigate() {
		driver.navigate().to(URL);
		LoginPage.redirectToNewUIifNeeded(driver);
		if (!driver.getCurrentUrl().contains("curriculum")) {
			driver.navigate().to(URL);
		}
	}
	
	public void clickCreateNew() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			log.error(e);
		}
		WebElement newCurrBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("curDashCrNewBtn")));
		newCurrBtn.click();
	}
	
	/**
	 * Searches and opens the edit page for the first curriculum whose name matches the given string
	 * @param curriculumName - the curriculum to search for
	 */
	public void editCurriculaByName(String curriculumName) {
		WebElement row = getRowByCurriculaName(curriculumName);
		List<WebElement> cells = row.findElements(By.tagName("td"));
		cells.get(cells.size()-1).findElement(By.id("curDashCurrEdit")).click();
	}
	
	private WebElement getRowByCurriculaName(String name) {
		wait.until(ExpectedConditions.visibilityOf(searchbox));
		searchbox.sendKeys(name);
		WebElement tbody = curriculaList.findElement(By.tagName("tbody"));
		List<WebElement> curriculaNameList = tbody.findElements(By.tagName("tr"));
		for (int i = 0; i < curriculaNameList.size(); i++) {
			WebElement row = curriculaNameList.get(i);
			String currentName = row.findElement(By.tagName("td")).getText();
			if (currentName.trim().equalsIgnoreCase(name)) {
				return row;
			}
		}
		return null;
	}
}
