package com.andrew.revpro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
	private static final Logger log = LogManager.getLogger();
    public static final String REVPRO_URL = "https://app.revature.com/core/login";
    public static final String QUIZ_LIB_URL = "https://app.revature.com/admin-v2/quiz/dashboard";
    public static Path EXAM_URL_FILEPATH = Paths.get(System.getProperty("user.home"),"Documents","imocha-uploads","revpro-exam-urls.txt");
    public static String SEARCH_TERM = "Exam:";
    public static enum APPLICATION_MODES { SEARCH, SCRAPE, CREATE_CURRICULUM, EXTRACT_CURRICULUM }
    public static APPLICATION_MODES MODE;
    public static boolean MULTITHREADING_ENABLED;

    private String username;
    private String password;
    private List<String> quizUrls;

    public Config(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public static Config loadConfiguration(String[] args) throws IOException {
    	String username = System.getenv("REVPRO_USERNAME");
    	String password = System.getenv("REVPRO_PW");
    	log.debug("REVPRO_USERNAME: "+username);
    	log.debug("REVPRO_PW: "+password);
    	if (username == null || password == null) {
    		log.error("Must provide REVPRO_USERNAME and REVPRO_PW environment variables");
    		System.err.println("Must provide REVPRO_USERNAME and REVPRO_PW environment variables");
    		throw new RuntimeException("Invalid startup configuration - environment vars not defined");
    	}
    	if (args.length < 1) {
    		log.error("Must specify either 'search' or 'scrape' command: 'java -jar app.jar scrape'");
    		throw new RuntimeException("Invalid startup configuration - command not specified");
    	}
    	Config conf = new Config(username, password);
    	if (args[0].equals("search")) {
    		log.debug("Setting search mode");
    		Config.MODE = APPLICATION_MODES.SEARCH;
    		// all other args are search terms
    		Config.SEARCH_TERM = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
    	} else if (args[0].equals("scrape")) {
    		log.debug("Setting scrape mode");
    		Config.MODE = APPLICATION_MODES.SCRAPE;
    		conf.setQuizUrls(readQuizUrls());
    		log.debug("Loaded "+ conf.quizUrls.size() + " quiz urls to extract");
    		if (args.length > 1 && args[1].equals("-m")) {
    			MULTITHREADING_ENABLED = true;
    			log.debug("Multithreading enabled");
    		}
    	} else if (args[0].equals("create")) {
    		Config.MODE = APPLICATION_MODES.CREATE_CURRICULUM;
    		log.debug("Setting CREATE_CURRICULUM mode");
    	} else if (args[0].equals("extract")) {
    		Config.MODE = APPLICATION_MODES.EXTRACT_CURRICULUM;
    		log.debug("Setting CREATE_CURRICULUM mode");
    	} else if (args[0].equals("help")) {
    		System.out.println("Usage:");
    		System.out.println("  java -jar app.jar search <searchTerms>                extract exam URLs by search term");
    		System.out.println("  java -jar app.jar scrape                              extract exam data to Excel");
    		System.out.println("  java -jar app.jar scrape -m                           use mulithreading");
    	}
    	log.info("Configuration loaded");
    	return conf;
    }
    
    private static List<String> readQuizUrls() throws IOException {
        return Files.readAllLines(EXAM_URL_FILEPATH);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getQuizUrls() {
        return quizUrls;
    }
    
    public void setQuizUrls(List<String> quizUrls) {
    	this.quizUrls = quizUrls;
    }
}
