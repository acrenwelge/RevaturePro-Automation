package com.andrew.revpro;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverFactory {

	public static WebDriver createChromeDriver() {
        System.out.println("Initializing system property: " + Config.CHROME_PROP);
        System.setProperty(Config.CHROME_PROP, System.getenv(Config.CHROME_DRIVER_EXEC));

        WebDriver driver = new ChromeDriver();
        return driver;
	}
	
}
