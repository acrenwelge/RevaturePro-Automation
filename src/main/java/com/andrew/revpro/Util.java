package com.andrew.revpro;

import java.util.ArrayList;
import java.util.List;

import com.andrew.revpro.model.Activity;
import com.andrew.revpro.model.ActivityType;

public class Util {
	
	private Util() {}
	
	public static String removeBulletPoints(String source) {
    	// creating bullet point via Unicode value
        int bullet = 0x2022;
        String bulletString = Character.toString((char) bullet);
        return source.replaceAll(bulletString, ""); // remove all bullet points
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
}
