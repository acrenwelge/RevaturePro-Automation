package com.andrew.revpro.pom;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.andrew.revpro.model.quiz.Question;
import com.andrew.revpro.model.quiz.Question.QuestionType;
import com.andrew.revpro.model.quiz.Quiz;

public class QuizPage {
	private WebDriver driver;
	
	@FindBy(id = "quesDetails")
	WebElement modal;
	
	@FindBy(css = "#quizVw th > div")
	List<WebElement> questionLinks;
	
	public QuizPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public Quiz extractQuizData() {
		Quiz quiz = new Quiz();
		WebDriverWait titleWait = new WebDriverWait(driver,10);
		WebElement title = titleWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quizVwName")));
		quiz.quizName = title.getText();
		for (WebElement link : questionLinks) {
			link.click();
			WebDriverWait wait = new WebDriverWait(driver, 10);
			modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quesDetails")));
			Question ques = new Question();
			fillQuestionType(ques);
			if (ques.type.equals(QuestionType.MATCHING)) {
				System.out.println("Skipping matching question");
				driver.findElement(By.cssSelector("#questionHead button")).click();
				continue;
			}
			fillQuestionText(ques);
			fillQuestionAnswerChoices(ques);
			quiz.addQuestion(ques);
			driver.findElement(By.cssSelector("#questionHead button")).click();
		}
		return quiz;
	}
	
	public void fillQuestionType(Question ques) {
		String val = modal.findElements(By.cssSelector("#quesDetails > div")).get(3)
			.findElements(By.cssSelector("div > div"))
			.get(1).getText();
		ques.type = Question.parseQuestionType(val);
	}
	
	public void fillQuestionText(Question ques) {
		WebElement dataDivs = modal.findElements(By.cssSelector("#quesDetails > div")).get(3);
		ques.questionText = dataDivs.findElements(By.cssSelector("div > div")).get(3).getText();
		if (dataDivs.findElements(By.tagName("div")).get(4).getText().equals("Question Content")) {
			ques.questionText += System.lineSeparator();
			ques.questionText += dataDivs.findElements(By.tagName("div")).get(5).getText();
		}
	}
	
	public void fillQuestionAnswerChoices(Question ques) {
		List<WebElement> rows = modal.findElements(By.cssSelector("#quesDetails > div"))
				.get(4).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr"));
		if ((ques.type.equals(QuestionType.BEST_CHOICE) || ques.type.equals(QuestionType.MULTI_CHOICE)) 
				&& rows.size() < 4 || rows.size() > 5) {
			System.err.println("WARNING: Skipping because fewer than 4 or more than 5 answer choices on question: "
				+ System.lineSeparator() + ques.questionText);
			return;
		}
		List<WebElement> row1 = rows.get(0).findElements(By.tagName("td"));
		List<WebElement> row2 = rows.get(1).findElements(By.tagName("td"));
		int lastCellIdxInRow = row1.size()-1;
		ques.ansA = row1.get(0).getText();
		ques.ansB = row2.get(0).getText();
		boolean AisRight = row1.get(lastCellIdxInRow).getText().strip().equals("Yes");
		boolean BisRight = row2.get(lastCellIdxInRow).getText().strip().equals("Yes");
		if (AisRight) {ques.correctAns = ques.correctAns.isBlank() ? "A" : ques.correctAns.concat(",A");}
		if (BisRight) {ques.correctAns = ques.correctAns.isBlank() ? "B" : ques.correctAns.concat(",B");}
		if (!ques.type.equals(QuestionType.TRUE_FALSE)) { // T/F questions only have 2 answer choices
			List<WebElement> row3 = rows.get(2).findElements(By.tagName("td"));
			List<WebElement> row4 = rows.get(3).findElements(By.tagName("td"));
			ques.ansC = row3.get(0).getText();
			ques.ansD = row4.get(0).getText();
			boolean CisRight = row3.get(lastCellIdxInRow).getText().strip().equals("Yes");
			boolean DisRight = row3.get(lastCellIdxInRow).getText().strip().equals("Yes");
			if (CisRight) {ques.correctAns = ques.correctAns.isBlank() ? "C" : ques.correctAns.concat(",C");}
			if (DisRight) {ques.correctAns = ques.correctAns.isBlank() ? "D" : ques.correctAns.concat(",D");}
			if (rows.size() > 4) {
				List<WebElement> row5 = rows.get(4).findElements(By.tagName("td"));
				ques.ansE = row5.get(0).getText();
				boolean EisRight = row3.get(lastCellIdxInRow).getText().strip().equals("Yes");
				if (EisRight) {
					ques.correctAns = ques.correctAns.isBlank() ? "E" : ques.correctAns.concat(",E");
				}
			}
		}
	}
	
}
