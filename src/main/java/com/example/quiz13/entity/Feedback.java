package com.example.quiz13.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
@IdClass(value = FeedbackId.class)
public class Feedback {

	@Id
	@Column(name = "user_email")
	private String userEmail;

	@Id
	@Column(name = "quiz_id")
	private int quizId;

	@Id
	@Column(name = "ques_id")
	private int quesId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "user_phone")
	private String userPhone;

	@Column(name = "user_age")
	private int userAge;

	@Column(name = "answer")
	private String answer;

	@Column(name = "fillin_date")
	private LocalDate fillinDate;

	public Feedback() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Feedback(String userEmail, int quizId, int quesId, String userName, String userPhone, int userAge,
			String answer, LocalDate fillinDate) {
		super();
		this.userEmail = userEmail;
		this.quizId = quizId;
		this.quesId = quesId;
		this.userName = userName;
		this.userPhone = userPhone;
		this.userAge = userAge;
		this.answer = answer;
		this.fillinDate = fillinDate;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public int getQuizId() {
		return quizId;
	}

	public int getQuesId() {
		return quesId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public int getUserAge() {
		return userAge;
	}

	public String getAnswer() {
		return answer;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

}
