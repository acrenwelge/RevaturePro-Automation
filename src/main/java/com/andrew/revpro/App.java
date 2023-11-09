package com.andrew.revpro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.andrew.revpro.Config.APPLICATION_MODES;
import com.andrew.revpro.excel.ExcelQuizTemplateWriter;
import com.andrew.revpro.model.quiz.Quiz;
import com.andrew.revpro.pom.LoginPage;
import com.andrew.revpro.pom.QuizPage;

/**
 * This script automates the following tasks on RevaturePro
 * - searching the quiz library by keywords and saving all returned URLs
 * - extraction of quiz questions to excel files given a file of URLs
 */
public class App {
	private WebDriver driver;
	private LoginPage lp;
	private QuizPage qp;
	
	private static Config APP_CONFIG;
	
    public static void main(String[] args) throws IOException {
    	Config conf = loadConfiguration(args);
    	App app = new App(conf);
    	app.loginToSystem();
    	if (Config.MODE.equals(APPLICATION_MODES.SEARCH)) {
    		app.searchAllExams();
    	} else if (Config.MODE.equals(APPLICATION_MODES.SCRAPE)) {
    		app.extractQuizzesToExcel();
    	}
    }
    
    protected static Config loadConfiguration(String[] args) throws IOException {
    	String username = System.getenv("REVPRO_USERNAME");
    	String password = System.getenv("REVPRO_PW");
    	if (username == null || password == null) {
    		System.err.println("Must provide REVPRO_USERNAME and REVPRO_PW environment variables");
    		System.exit(1); // Exit the application if the configuration is missing
    	}
    	if (args.length < 1) {
    		System.err.println("Must specify either 'search' or 'scrape' command: 'java -jar app.jar scrape'");
    		System.exit(1); // Exit the application if no args provided
    	}
    	Config conf = new Config(username, password);
    	if (args[0].equals("search")) {
    		Config.MODE = APPLICATION_MODES.SEARCH;
    		// all other args are search terms
    		Config.SEARCH_TERM = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
    	} else if (args[0].equals("scrape")) {
    		Config.MODE = APPLICATION_MODES.SCRAPE;
    		conf.setQuizUrls(readQuizUrls());
    	}
    	return new Config(username, password);
    }
     
    public App(Config conf) {
    	APP_CONFIG = conf;
		this.driver = WebDriverFactory.createChromeDriver();
	}
    
    protected static List<String> readQuizUrls() throws IOException {
        Path file = Paths.get(System.getProperty("user.home"),"Documents","imocha-uploads","revpro-exam-urls.txt");
        return Files.readAllLines(file);
    }
    
    protected void loginToSystem() {
    	driver.get(Config.REVPRO_URL);
    	driver.manage().window().maximize();
    	this.lp = new LoginPage(driver);
    	lp.loginToSystem(APP_CONFIG.getUsername(), APP_CONFIG.getPassword());
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    }
    
    protected void searchAllExams() {
    	driver.get(Config.QUIZ_LIB_URL);
    	WebDriverWait wait = new WebDriverWait(driver, 10);
    	WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchtext")));
    	input.sendKeys(Config.SEARCH_TERM);
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
    
    protected void extractQuizzesToExcel() throws IOException {
    	for (String url : APP_CONFIG.getQuizUrls()) {
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
