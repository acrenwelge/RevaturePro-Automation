package com.andrew.revpro.model.quiz;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
	public String quizName;
	public List<Question> questions = new ArrayList<>();
	
	public void addQuestion(Question q) {
		this.questions.add(q);
	}
}
