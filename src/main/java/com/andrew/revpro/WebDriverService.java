package com.andrew.revpro;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverService {
	
	private static final String CHROME_PROP = "webdriver.chrome.driver";
	private static final String CHROME_DRIVER_EXEC = "CHROMEDRIVER"; // the environment variable with the path to the executable
	
	private static WebDriver driver;
	
	public static WebDriver getDriver() {
		if (driver == null) {
			System.out.println("initializing system property: " + CHROME_PROP);
			System.setProperty(CHROME_PROP, System.getenv(CHROME_DRIVER_EXEC));
			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			return driver;
		} else {
			return driver;
		}
	}
}
