package com.andrew.revpro;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.andrew.revpro.pom.LoginPage;

/**
 * This script automates the entering of curricula information into the RevaturePro system
 */
public class App {
	private WebDriver driver;
	private LoginPage lp;
	
	private static final String REVPRO_URL = "https://app.revature.com/core/login";
	private static final String CHROME_PROP = "webdriver.chrome.driver";
	private static final String CHROME_DRIVER_EXEC = "CHROMEDRIVER"; // the environment variable with the path to the executable
	
	private static boolean EXTRACT_ONLY = false;
	
	private static String USERNAME;
	private static String PASSWORD;
	
    public static void main(String[] args) {
    	if (args[0].equals("--extract-only")) {
    		EXTRACT_ONLY = true;
    	} else {
    		USERNAME = args[0];
    		PASSWORD = args[1];
    	}
    	App a = new App();
    	if (EXTRACT_ONLY) {
    		a.extractExcel();
    	} else {
    		a.automate();
    	}
    }
     
    public App() {
    	// initialize driver
		System.out.println("initializing system property: " + CHROME_PROP);
		System.setProperty(CHROME_PROP, System.getenv(CHROME_DRIVER_EXEC));
		this.driver = new ChromeDriver();
		this.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
		// initialize LoginPage
		this.lp = new LoginPage(driver);
	}
    
    private void automate() {
    	driver.get(REVPRO_URL);
    	lp.loginToSystem(USERNAME, PASSWORD);
    }
    
    private void extractExcel() {
    	String cellValue = "";
    	String finalValue = removeBulletPoints(cellValue);
    }
    
    private String removeBulletPoints(String source) {
    	// creating bullet point via Unicode value
        int bullet = 0x2022;
        String bulletString = Character.toString((char) bullet);
        return source.replaceAll(bulletString, ""); // remove all bullet points
    }
}
