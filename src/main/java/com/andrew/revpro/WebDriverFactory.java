package com.andrew.revpro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverFactory {
	private static Logger log = LogManager.getLogger();
	private static final String COOKIE_FILE = "cookies.ser";

	public static WebDriver createChromeDriver() {
        log.info("Initializing system properties: ");
        log.info(" webdriver.chrome.driver - "+System.getenv("CHROMEDRIVER"));
        System.setProperty("webdriver.chrome.driver", System.getenv("CHROMEDRIVER"));
        System.setProperty("webdriver.edge.driver", "C:\\edgedriver\\msedgedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary(System.getenv("CHROME"));

        WebDriver driver = new ChromeDriver(options);
        return driver;
	}

    public static void writeCookiesToFile(Set<Cookie> cookies) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(COOKIE_FILE))) {
            outputStream.writeObject(cookies);
        } catch (IOException e) {
            log.error(e);
        }
    }

    @SuppressWarnings("unchecked")
	public static Optional<Set<Cookie>> readCookiesFromFile() {
    	File file = Paths.get(COOKIE_FILE).toFile();
		try {
			file.createNewFile();
		} catch (IOException e) {
			log.error(e);
			return Optional.empty();
		}
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(COOKIE_FILE))) {
            return Optional.of((Set<Cookie>) inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            log.error(e);
            return Optional.empty();
        }
    }
	
}
