package com.andrew.revpro;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.andrew.revpro.Config.APPLICATION_MODES;
import com.andrew.revpro.curriculum.data.Curriculum;
import com.andrew.revpro.excel.ExcelCurriculumWriter;
import com.andrew.revpro.excel.ExcelQuizTemplateWriter;
import com.andrew.revpro.pages.CurriculaListPage;
import com.andrew.revpro.pages.CurriculumEditorPage;
import com.andrew.revpro.pages.LoginPage;
import com.andrew.revpro.pages.NewCurriculumPage;
import com.andrew.revpro.pages.QuizPage;
import com.andrew.revpro.quiz.data.Quiz;

/**
 * This script automates the following tasks on RevaturePro
 * - searching the quiz library by keywords and saving all returned URLs
 * - extraction of quiz questions to excel files given a file of URLs
 */
public class App {
	private static final Logger log = LogManager.getLogger();
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage lp;
	private QuizPage qp;
	
	private static Config APP_CONFIG;
	
    public static void main(String[] args) throws IOException {
    	APP_CONFIG = Config.loadConfiguration(args);
    	if (Config.MODE.equals(APPLICATION_MODES.SEARCH)) {
    		App app = new App();
        	app.navigateAndLogin();
    		app.searchAllExams();
    		app.shutdown();
    	} else if (Config.MODE.equals(APPLICATION_MODES.SCRAPE)) {
    		if (Config.MULTITHREADING_ENABLED) {
    			scrapeMultithreaded();
    		} else {
    			App app = new App();
    			app.navigateAndLogin();
    			app.extractQuizzesToExcel(APP_CONFIG.getQuizUrls());
    			app.shutdown();
    		}
    	} else if (Config.MODE.equals(APPLICATION_MODES.CREATE_CURRICULUM)) {
    		App app = new App();
			app.navigateAndLogin();
			app.createNewCurriculum();
			app.shutdown();
    	} else if (Config.MODE.equals(APPLICATION_MODES.EXTRACT_CURRICULUM)) {
    		App app = new App();
			app.navigateAndLogin();
			app.extractCurriculum();
			app.shutdown();
    	}
    }
    
    public App() {
		this.driver = WebDriverFactory.createChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		this.wait = new WebDriverWait(driver, 5);
	}
    
    protected static void scrapeMultithreaded() {
    	// Use fewer than the available processors so that user is not slowed down
        int threadPoolSize = Runtime.getRuntime().availableProcessors() / 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        // each task will be extracting a set of quiz urls to excel files
        log.debug("Dividing " + APP_CONFIG.getQuizUrls().size() + " quiz urls to " + threadPoolSize + " threads.");
        List<Runnable> tasks = createTasks(threadPoolSize, APP_CONFIG.getQuizUrls());
        for (Runnable task : tasks) {
            executorService.submit(task);
        }
        executorService.shutdown();
        try {
            // Wait for all tasks to complete or a timeout to occur
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                System.err.println("Not all tasks completed within the timeout.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates Runnable tasks that launch WebDriver, login to the system, and extract a subset
     * of quizzes that it has been given
     */
    protected static List<Runnable> createTasks(int numThreads, List<String> allQuizUrls) {
    	List<List<String>> groupedQuizUrls = groupQuizUrls(numThreads, allQuizUrls);
        List<Runnable> tasks = new ArrayList<>();
        for (final List<String> urls : groupedQuizUrls) {
            tasks.add(() -> {
            	// each task has its own App (and therefore WebDriver) instance that it manages
                App app = new App();
            	app.navigateAndLogin();
            	log.debug("Thread "+Thread.currentThread().getName() + " logged in and proceeding to quiz extraction");
                app.extractQuizzesToExcel(urls);
                log.debug("Thread "+Thread.currentThread().getName() + " completed quiz extraction - shutting down");
                app.shutdown();
            });
        }
        return tasks;
    }
    
    /**
     * Divide the URL strings into groups depending on number of threads available
     */
    protected static List<List<String>> groupQuizUrls(int numThreads, List<String> allQuizUrls) {
    	int originalSize = allQuizUrls.size();
        int sublistSize = originalSize / numThreads;
        int remainder = originalSize % numThreads;
        List<List<String>> groupedUrls = IntStream.range(0, numThreads)
                .mapToObj(i -> allQuizUrls.subList(i * sublistSize + Math.min(i, remainder),
                        (i + 1) * sublistSize + Math.min(i + 1, remainder)))
                .collect(Collectors.toList());
        return groupedUrls;
    }
    
    protected void navigateAndLogin() {
    	this.lp = new LoginPage(driver);
    	try {
    		lp.navigate();
			Thread.sleep(1000);
			Optional<Set<Cookie>> cookies = WebDriverFactory.readCookiesFromFile();
			cookies.ifPresent(set -> set.forEach(cookie -> driver.manage().addCookie(cookie)));
			if (cookies.isPresent()) { // bypass need for login if auth cookies loaded
				return;
			}
    		lp.loginToSystem(APP_CONFIG.getUsername(), APP_CONFIG.getPassword());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    }
    
    protected void searchAllExams() throws IOException {
    	log.debug("starting search...");
    	driver.get(Config.QUIZ_LIB_URL);
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
    	Files.createDirectories(Config.EXAM_URL_FILEPATH);
    	BufferedWriter bw = Files.newBufferedWriter(Config.EXAM_URL_FILEPATH);
    	bw.write(""); // clear the file
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
    		log.debug("Found URL: " + driver.getCurrentUrl());
    		String quizURL = driver.getCurrentUrl();
    		bw.append(quizURL);
    		count++;
    		driver.navigate().back();
    	}
    	bw.close();
    }
    
    protected void extractQuizzesToExcel(List<String> quizUrls) {
    	log.debug("starting quiz extraction");
    	this.qp = new QuizPage(driver);
    	for (String url : quizUrls) {
    		driver.navigate().to(url);
    		Quiz quiz = qp.extractQuizData();
    		try {
    			ExcelQuizTemplateWriter.writeQuizToNewExcelFile(quiz);
    			log.info("Completed: " + url);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    protected void createNewCurriculum() {
    	CurriculaListPage clp = new CurriculaListPage(driver);
		clp.navigate();
		clp.clickCreateNew();
		boolean check = driver.getCurrentUrl().equals("https://app.revature.com/admin-v2/curriculum/create");
		System.out.println(check);
		NewCurriculumPage ncp = new NewCurriculumPage(driver);
		ncp.setCurriculumNameField("test");
		ncp.selectDefaultSchedule();
		ncp.toggleShowDescription();
		ncp.enterTags(Arrays.asList("FOO","BAR"));
		ncp.setCompetency("Java SQL Rest");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ncp.cancel();
    }
    
    protected void extractCurriculum() {
    	CurriculaListPage clp = new CurriculaListPage(driver);
		clp.navigate();
		clp.editCurriculaByName("GENAI DEVELOPER - BRIGHTSPEED");
		CurriculumEditorPage editor = new CurriculumEditorPage(driver);
		Curriculum curr = editor.extractEntireCurriculum();
		try {
			ExcelCurriculumWriter.writeQuizToNewExcelFile(curr);
		} catch (IOException e) {
			log.error(e);
		}
    }
    
    protected void shutdown() {
    	// persist the JSession ID and Auth cookies for next time
        Cookie sessionid = driver.manage().getCookieNamed("JSESSIONID");
        Cookie authtoken = driver.manage().getCookieNamed("authToken");
        Set<Cookie> cookies = new HashSet<>();
        cookies.add(sessionid);
        cookies.add(authtoken);
    	WebDriverFactory.writeCookiesToFile(cookies);
    	log.debug("Closing WebDriver instance");
    	driver.quit();
    }
}
