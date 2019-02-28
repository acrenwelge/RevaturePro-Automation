package com.andrew.revpro;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.andrew.revpro.excel.CurriculumReader;
import com.andrew.revpro.model.Curriculum;
import com.andrew.revpro.pom.CurriculumEditorModal;
import com.andrew.revpro.pom.CurriculumEditorPage;
import com.andrew.revpro.pom.LoginPage;
import com.andrew.revpro.pom.NewCurriculumPage;

/**
 * This script automates the entering of curricula information into the RevaturePro system
 */
public class App {
	RevProService service;
	
	private static final String PROPS_FILE = "config.properties";
	private static final Properties config = new Properties();
	private static boolean prod = false;
	
    public static void main(String[] args) {
    	File curriculumExcelFile = new File(args[0]);
    	try(InputStream input = App.class.getClassLoader().getResourceAsStream(PROPS_FILE)) {
    		if (input == null) {
    			System.err.println("Cannot find " + PROPS_FILE + " file");
    			return;
    		}
    		config.load(input);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	Curriculum curr = extractCurriculum(curriculumExcelFile);
    	App a = new App();
    	a.automate(curr);
    }
     
    public App() {
    	// initialize driver
		WebDriver d = WebDriverService.getDriver();
		
		// initialize LoginPage
		LoginPage lp = new LoginPage(d);

		String url = config.getProperty("dev.url");
		String username = config.getProperty("dev.username");
		String password = config.getProperty("dev.password");
		if (prod) {
			url = config.getProperty("prod.url");
			username = config.getProperty("prod.username");
			password = config.getProperty("prod.password");
		} else {
			url = config.getProperty("dev.url");
			username = config.getProperty("dev.username");
			password = config.getProperty("dev.password");
		}
		NewCurriculumPage ncp = new NewCurriculumPage(d);
		CurriculumEditorModal cem = new CurriculumEditorModal(d);
		CurriculumEditorPage cep = new CurriculumEditorPage(d, cem);
		this.service = new RevProDevService(cep, cem, ncp, lp, url, username, password);
	}
    
    private static Curriculum extractCurriculum(File excelFile) {
    	CurriculumReader cr = new CurriculumReader();
    	return cr.getCurriculum(excelFile);
    }
    
    public void automate(Curriculum c) {
    	service.createCurriculumFromTemplate(c);
    }
    
}
