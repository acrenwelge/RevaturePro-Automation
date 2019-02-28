package com.andrew.revpro;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.andrew.revpro.model.Activity;
import com.andrew.revpro.model.ActivityType;

public class Util {
	
	private Util() {}
	
	public static String removeBulletPoints(String source) {
    	// creating bullet point via Unicode value
        int bullet1 = 0x2022;
		int bullet2 = 9679;
        String bulletString1 = Character.toString((char) bullet1);
        String bulletString2 = Character.toString((char) bullet2);
        String stripped = source.replaceAll(bulletString1, "").trim(); // remove all bullet points and cut off whitespace
        return stripped.replaceAll(bulletString2, "").trim(); // again for the other kind of bullet
    }
	
	public static List<Activity> extractActivitiesFromBulletPoints(String bullets) {
		String stripped = removeBulletPoints(bullets);
		String[] arr = stripped.split("\n");
		List<Activity> list = new ArrayList<>();
		for (String str : arr) {
			list.add(new Activity(str, ActivityType.LECTURE));
		}
		return list;
	}
	
	public static void jsClick(WebDriver d, WebElement we) {
		JavascriptExecutor jse = (JavascriptExecutor) d;
		jse.executeScript("arguments[0].click()", we);
	}
	
	public static void explicitlySleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
