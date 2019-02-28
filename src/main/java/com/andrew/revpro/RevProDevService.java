package com.andrew.revpro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.andrew.revpro.model.Curriculum;
import com.andrew.revpro.model.CurriculumWeek;
import com.andrew.revpro.pom.CurriculumEditorModal;
import com.andrew.revpro.pom.CurriculumEditorPage;
import com.andrew.revpro.pom.LoginPage;
import com.andrew.revpro.pom.NewCurriculumPage;

public class RevProDevService extends RevProService {
	
	WebDriver driver = WebDriverService.getDriver();
	WebDriverWait wait = new WebDriverWait(driver, 10);
	
	public RevProDevService(CurriculumEditorPage cep, CurriculumEditorModal cem, NewCurriculumPage ncp, LoginPage lp, String url, String uname, String pword) {
		super(cep, cem, ncp, lp, url, uname, pword);
	}

	@Override
	public void createCurriculumFromTemplate(Curriculum curriculum) {
		login();
		Util.explicitlySleep(5000);
		goToCurricula();
		// click create new button
		Util.explicitlySleep(10000);
		WebElement createNewBtn = driver.findElement(By.xpath("//*[@id=\"app-main\"]/app-curriculum-dashboard/div[1]/div[1]/div/div[2]/button[1]"));
		wait.until(ExpectedConditions.elementToBeClickable(createNewBtn)).click();
		fillOutNewCurriculumInfo(curriculum);
		Util.explicitlySleep(5000);
		// now on the curriculum editor page
		int week = 1;
		cep.clickEditNthWeek(week);
		for (CurriculumWeek cw : curriculum.getWeeks()) {
			// TODO: add the week metadata: name, subtitle, environment, topics, etc...
			// don't click new week button first time
			if (week != 1) cep.getNewWeekBtn().click();
			cep.clickEditNthWeek(week);
			cep.enterWeekActivities(week, cw);
		}
		// hit the back button
	}
	
	private void goToCurricula() {
		// go to the new UI
		WebElement newUiBtn = driver.findElement(By.xpath("//*[@id=\"newUI\"]/a"));
		wait.until(ExpectedConditions.elementToBeClickable(newUiBtn));
		Util.jsClick(driver, newUiBtn);
		// click library
		WebElement library = driver.findElement(By.xpath("//*[@id=\"slide-out\"]/li[2]/ul/mdb-squeezebox/div/mdb-item[4]/div/mdb-item-head/div/a/h5/span[1]"));
		wait.until(ExpectedConditions.elementToBeClickable(library)).click();
		// click curriculum
		WebElement currBtn = driver.findElement(By.xpath("//*[@id=\"slide-out\"]/li[2]/ul/mdb-squeezebox/div/mdb-item[4]/div/mdb-item-body/div/div/ul/li/a/span[2]"));
		wait.until(ExpectedConditions.elementToBeClickable(currBtn)).click();
	}

}
