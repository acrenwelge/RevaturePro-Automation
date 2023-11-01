package com.andrew.revpro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.andrew.revpro.excel.ExcelQuizTemplateWriter;
import com.andrew.revpro.model.quiz.Quiz;
import com.andrew.revpro.pom.LoginPage;
import com.andrew.revpro.pom.QuizPage;

/**
 * This script automates the extraction of quiz questions to excel files
 */
public class App {
	private WebDriver driver;
	private LoginPage lp;
	private QuizPage qp;
	
	private static final String REVPRO_URL = "https://app.revature.com/core/login";
	private static final String CHROME_PROP = "webdriver.chrome.driver";
	private static final String CHROME_DRIVER_EXEC = "CHROMEDRIVER"; // the environment variable with the path to the executable
	
	private static String USERNAME;
	private static String PASSWORD;
	private static List<String> QUIZ_URLS;
	
    public static void main(String[] args) {
    	USERNAME = System.getenv("REVPRO_USERNAME");
    	PASSWORD = System.getenv("REVPRO_PW");
    	if (USERNAME == null || PASSWORD == null) {
    		System.err.println("Must provide REVPRO_USERNAME and REVPRO_PW environment variables");
    	}
    	if (args.length != 1) {
    		System.err.println("Must specify either 'search' or 'scrape' command: 'java -jar app.jar scrape'");
    	}
        try {
            Path file = Paths.get(System.getProperty("user.home"),"Documents","imocha-uploads","revpro-exam-urls.txt");
            QUIZ_URLS = Files.readAllLines(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    	App app = new App();
    	app.loginToSystem();
    	if (args[0].equals("search")) {
    		app.searchAllExams();
    	} else if (args[0].equals("scrape")) {
    		app.extractQuizzesToExcel();    		
    	}
    }
     
    public App() {
    	// initialize driver
		System.out.println("initializing system property: " + CHROME_PROP);
		System.setProperty(CHROME_PROP, System.getenv(CHROME_DRIVER_EXEC));
		this.driver = new ChromeDriver();
		this.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}
    
    private void loginToSystem() {
    	driver.get(REVPRO_URL);
    	driver.manage().window().maximize();
    	this.lp = new LoginPage(driver);
    	lp.loginToSystem(USERNAME, PASSWORD);
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    }
    
    private void searchAllExams() {
    	driver.get("https://app.revature.com/admin-v2/quiz/dashboard");
    	WebDriverWait wait = new WebDriverWait(driver, 10);
    	WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchtext")));
    	input.sendKeys("Exam:");
    	input.sendKeys(Keys.RETURN);
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	List<WebElement> rows = driver.findElement(By.id("quizListTblDiv")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
    	final int total = rows.size();
    	int count = 0;
    	while (count < total) {
        	WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchtext")));
        	searchInput.clear();
        	searchInput.sendKeys("Exam:");
        	searchInput.sendKeys(Keys.RETURN);
        	try {
    			Thread.sleep(2000);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		List<WebElement> trs = driver.findElement(By.id("quizListTblDiv")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
    		trs.get(count).findElement(By.tagName("td")).findElement(By.tagName("span")).click();
    		System.out.println(driver.getCurrentUrl());
    		count++;
    		driver.navigate().back();
    	}
    	driver.quit();
    }
    
    private void extractQuizzesToExcel() {
    	for (String url : QUIZ_URLS) {
    		driver.navigate().to(url);
    		this.qp = new QuizPage(driver);
    		Quiz quiz = qp.extractQuizData();
    		try {
    			ExcelQuizTemplateWriter.writeQuizToNewExcelFile(quiz);
    			System.out.println("Completed: " + url);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	driver.quit();
    }
}
