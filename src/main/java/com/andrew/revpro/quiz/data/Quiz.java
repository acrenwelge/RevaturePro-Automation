package com.andrew.revpro.quiz.data;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
	public class QuizParseStats {
		public int totalQuestions;
		public int skipped;
	}
	
	public String quizName;
	public List<Question> questions = new ArrayList<>();
	public QuizParseStats stats = new QuizParseStats();
	
	public void addQuestion(Question q) {
		this.questions.add(q);
	}
}
