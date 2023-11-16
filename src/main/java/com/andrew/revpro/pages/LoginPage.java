package com.andrew.revpro.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.andrew.revpro.Config;

public class LoginPage {
	private WebDriver driver;
	private static final Logger log = LogManager.getLogger();
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(id = "loginForm:userName-input-id")
	WebElement usernameField;
	
	@FindBy(id = "loginForm:input-psw")
	WebElement passwordField;
	
	@FindBy(id = "loginForm:login-btn-id")
	WebElement loginBtn;
	

	public void navigate() {
		driver.get(Config.REVPRO_URL);
		driver.manage().window().maximize();
	}
	
	public void loginToSystem(String username, String password) {
		usernameField.sendKeys(username);
		passwordField.sendKeys(password);
		loginBtn.click();
	}
	
	/**
	 * Sometimes RevaturePro kicks you out to the "old UI" page and you need to click the "Go to New Curriculum" button.
	 * This method waits to redirect you, or ignores the timeout exception if you're already in the new UI portal
	 * @param driver - the WebDriver driver to use
	 */
	public static void redirectToNewUIifNeeded(WebDriver driver) {
		try { // if the old UI loads for some reason
			WebElement portalLink = new WebDriverWait(driver, 5)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboardForm:goToNewUIBtn")));
			portalLink.click();
		} catch (TimeoutException e) {
			log.debug(e);
			// no problem :) we should be on the new UI
		}
	}
}
