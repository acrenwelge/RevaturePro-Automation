package com.andrew.revpro;

import com.andrew.revpro.model.Curriculum;
import com.andrew.revpro.pom.CurriculumEditorModal;
import com.andrew.revpro.pom.CurriculumEditorPage;
import com.andrew.revpro.pom.LoginPage;
import com.andrew.revpro.pom.NewCurriculumPage;

public class RevProProdService extends RevProService {

	public RevProProdService(CurriculumEditorPage cep, CurriculumEditorModal cem, NewCurriculumPage ncp, LoginPage lp, String url, String uname, String pword) {
		super(cep, cem, ncp, lp, url, uname, pword);
	}

	@Override
	public void createCurriculumFromTemplate(Curriculum curriculum) {
		login();
	}

}
