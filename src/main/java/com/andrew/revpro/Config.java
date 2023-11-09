package com.andrew.revpro;

import java.util.List;

public class Config {
    public static final String REVPRO_URL = "https://app.revature.com/core/login";
    public static final String QUIZ_LIB_URL = "https://app.revature.com/admin-v2/quiz/dashboard";
    public static final String CHROME_PROP = "webdriver.chrome.driver";
    public static final String CHROME_DRIVER_EXEC = "CHROMEDRIVER"; // the environment variable with the path to the executable
    public static String SEARCH_TERM = "Exam:";
    public static enum APPLICATION_MODES { SEARCH, SCRAPE }
    public static APPLICATION_MODES MODE;

    private String username;
    private String password;
    private List<String> quizUrls;

    public Config(String username, String password) {
        this.username = username;
        this.password = password;
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
