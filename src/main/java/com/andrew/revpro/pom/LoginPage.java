package com.andrew.revpro.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
	private WebDriver driver;
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebElement getUsernameField() {
		return driver.findElement(By.id("loginForm:userName-input-id"));
	}
	
	public WebElement getPasswordField() {
		return driver.findElement(By.id("loginForm:input-psw"));
	}
	
	public WebElement getLoginButton() {
		return driver.findElement(By.id("loginForm:login-btn-id"));
	}
	
	public void loginToSystem(String username, String password) {
		getUsernameField().sendKeys(username);
		getPasswordField().sendKeys(password);
		getLoginButton().click();
	}
}
