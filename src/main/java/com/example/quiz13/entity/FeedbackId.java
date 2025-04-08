package com.example.quiz13.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FeedbackId implements Serializable {

	private String userEmail;

	private int quizId;

	private int quesId;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

}
