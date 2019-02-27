package com.andrew.revpro;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.andrew.revpro.excel.CurriculumReader;
import com.andrew.revpro.model.Curriculum;
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
	
	private static String USERNAME;
	private static String PASSWORD;
	private static File CurriculumExcelFile;
	
    public static void main(String[] args) {
    	USERNAME = args[0];
    	PASSWORD = args[1];
    	CurriculumExcelFile = new File(args[2]);
    	extractExcel();
    	System.exit(0); // for now... just want to check the excel extraction
    	App a = new App();
    	a.automate();
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
    
    private static void extractExcel() {
    	CurriculumReader cr = new CurriculumReader();
    	Curriculum c = cr.getCurriculum(CurriculumExcelFile);
    	System.out.println(c);
    }
    
    
}
