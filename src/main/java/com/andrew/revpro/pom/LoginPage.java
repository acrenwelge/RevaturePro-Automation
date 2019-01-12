package com.andrew.revpro.pom;

import java.util.Arrays;
import java.util.StringJoiner;

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
	
	private static void printBullets() {
    	// creating bullet point via Unicode value
        int bullet = 0x2022;
        String[] points = {"bullet point 1", "bullet point 2", "bullet point 3"};
        String bulletString = Character.toString((char) bullet);
        Arrays.asList(points).forEach(s -> {
        	System.out.println(bulletString + s);
        });
        System.out.println("======================");
        StringJoiner sj = new StringJoiner(bulletString + "\n");
        StringBuilder sb = new StringBuilder();
        for (String s : points) {
        	sj.add(s);
        	sb.append(bulletString + s + "\n");
        }
        System.out.println(sj);
        System.out.println("======================");
        System.out.println(sb);
    }
}
