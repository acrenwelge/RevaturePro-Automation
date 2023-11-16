package com.andrew.revpro;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.openqa.selenium.Cookie;

public class CookieWriter {

	public static void main(String[] args) {
//		Set<Cookie> cookies = new HashSet<>();
//		Cookie c1 = new Cookie("JSESSIONID","D7D68B4F704DF1C5B42C2B955B9F9B78");
//		Cookie c2 = new Cookie("authToken","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbXBJZCI6IjM4MCIsInJvbGUiOiJTUjAwMyIsInJvbGVzIjpbIlNSMDAzIiwiU1IwMTUiLCJTUjAxMyIsIlNSMDE0IiwiU1IwMDQiLCJTUjAxNyJdLCJpc3MiOiJSZXZhdHVyZVBybyIsInRpbWVab25lIjoiRVNUNUVEVCIsInNlc3Npb25JZCI6IkQ3RDY4QjRGNzA0REYxQzVCNDJDMkI5NTVCOUY5Qjc4IiwiYWN0dWFsX3VzZXIiOm51bGwsIm9yZ2FuaXphdGlvbiI6IjIiLCJuYW1lIjoiYW5kcmV3LmNyZW53ZWxnZUByZXZhdHVyZS5jb20iLCJvcmdhbml6YXRpb25zIjpbIjIiXSwicHJpbWFyeV9vcmdhbml6YXRpb24iOiIyIiwiZXhwIjoxNzAxMjcyNTMzLCJqdGkiOiIyNDE1In0.7b_JnYKFBW2YBfcPUVdEyqzdTPBj26VBoRt25E3ckwM");
//		cookies.add(c1);
//		cookies.add(c2);
//        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("cookies.ser"))) {
//            outputStream.writeObject(cookies);
//        } catch (IOException e) {
//            System.out.println(e);
//        }
		readCookies();
	}
	
	public static void readCookies() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("cookies.ser"))) {
            Optional<Set<Cookie>> opt = Optional.of((Set<Cookie>) inputStream.readObject());
            opt.get().forEach(c -> {
            	System.out.println(c.getName());
            	System.out.println(c.getValue());
            });
        } catch (IOException | ClassNotFoundException e) {
        	System.out.println(e);
        }
	}

}
