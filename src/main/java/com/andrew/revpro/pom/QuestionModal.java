package com.andrew.revpro.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class QuestionModal {
	private WebDriver driver;
	
	public QuestionModal(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getQuestionType() {
		return "";
	}
	
	public String getQuestionText() {
		WebElement modal = driver.findElement(By.id("quesDetails"));
		return "";
	}
	
	public String getQuestionAnswerChoices() {
		return "";
	}
	
	public String getCorrectAnswer() {
		return "";
	}
}
