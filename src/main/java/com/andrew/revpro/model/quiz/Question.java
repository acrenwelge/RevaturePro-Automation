package com.andrew.revpro.model.quiz;

public class Question {
	public String questionText;
	public String ansA;
	public String ansB;
	public String ansC;
	public String ansD;
	public String ansE;
	public String correctAns = "";
	public enum QuestionType {
		BEST_CHOICE, MULTI_CHOICE, TRUE_FALSE, MATCHING
	}
	public QuestionType type;
	public static QuestionType parseQuestionType(String value) {
		if (value.contains("Best Choice")) {
			return QuestionType.BEST_CHOICE;
		} else if (value.contains("Multiple Choice")) {
			return QuestionType.MULTI_CHOICE;
		} else if (value.contains("True")) {
			return QuestionType.TRUE_FALSE;
		} else if (value.contains("Matching")) {
			return QuestionType.MATCHING;
		}
		return QuestionType.BEST_CHOICE;
	}
}