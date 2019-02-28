package com.andrew.revpro;

import org.openqa.selenium.By;

import com.andrew.revpro.model.Curriculum;
import com.andrew.revpro.model.CurriculumDay;
import com.andrew.revpro.model.CurriculumWeek;
import com.andrew.revpro.pom.CurriculumEditorModal;
import com.andrew.revpro.pom.CurriculumEditorPage;
import com.andrew.revpro.pom.LoginPage;
import com.andrew.revpro.pom.NewCurriculumPage;

public class RevProDevService extends RevProService {
	
	public RevProDevService(CurriculumEditorPage cep, CurriculumEditorModal cem, NewCurriculumPage ncp, LoginPage lp, String url, String uname, String pword) {
		super(cep, cem, ncp, lp, url, uname, pword);
	}

	@Override
	public void createCurriculumFromTemplate(Curriculum curriculum) {
		login();
		goToCurricula();
		// click create new button
		WebDriverService.getDriver().findElement(By.xpath("//*[@id=\"app-main\"]/app-curriculum-dashboard/div[1]/div[1]/div/div[2]/button[1]")).click();
		fillOutNewCurriculumInfo(curriculum);
		int week = 1;
		cep.clickEditNthWeek(week);
		for (CurriculumWeek cw : curriculum.getWeeks()) {
			// TODO: add the week metadata: name, subtitle, environment, topics, etc...
			// don't click new week button first time
//			if (week != 1) cep.getNewWeekBtn().click();
//			cep.clickEditNthWeek(week);
			cep.enterWeekActivities(week, cw);
		}
		// hit the back button
	}
	
	private void goToCurricula() {
		WebDriverService.getDriver().findElement(By.xpath("//*[@id=\"slide-out\"]/li[2]/ul/mdb-squeezebox/div/mdb-item[4]/div/mdb-item-head/div/a/h5/span[1]")).click();
		WebDriverService.getDriver().findElement(By.xpath("//*[@id=\"slide-out\"]/li[2]/ul/mdb-squeezebox/div/mdb-item[4]/div/mdb-item-body/div/div/ul/li/a/span[2]")).click();
	}

}
