package com.andrew.revpro;

import com.andrew.revpro.model.Curriculum;
import com.andrew.revpro.pom.CurriculumEditorModal;
import com.andrew.revpro.pom.CurriculumEditorPage;
import com.andrew.revpro.pom.LoginPage;
import com.andrew.revpro.pom.NewCurriculumPage;

public abstract class RevProService {
	
	protected LoginPage lp;
	protected NewCurriculumPage ncp;
	protected CurriculumEditorPage cep;
	protected CurriculumEditorModal cem;
	
	protected String url;
	protected String username;
	protected String password;
	
	public RevProService(CurriculumEditorPage cep, CurriculumEditorModal cem, NewCurriculumPage ncp, LoginPage lp, String url, String uname, String pword) {
		this.ncp = ncp;
		this.cem = cem;
		this.cep = cep;
		this.url = url;
		this.username = uname;
		this.password = pword;
		this.lp = lp;
	}
	
	public abstract void createCurriculumFromTemplate(Curriculum curriculum);
	
	protected void login() {
    	WebDriverService.getDriver().get(url);
    	lp.loginToSystem(username, password);
    }
	
	protected void fillOutNewCurriculumInfo(Curriculum curriculum) {
		ncp.getCurriculumNameField().sendKeys(curriculum.getName());
		ncp.selectCurriculumType(curriculum.getCurrType());
		ncp.enterTags(curriculum.getTags());
		ncp.selectDefaultSchedule();
		ncp.selectProgramType(curriculum.getProgType());
		ncp.clickNext();
	}
	
}
