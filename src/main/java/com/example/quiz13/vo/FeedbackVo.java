package com.example.quiz13.vo;

import java.time.LocalDate;
import java.util.List;

public class FeedbackVo {

	private String userName;

	private String userPhone;

	private String userEmail;

	private int age;

	private List<QuesAnswerVo> quesAnswerList;

	private LocalDate fillinDate;

	public FeedbackVo() {
		super();
	}

	public FeedbackVo(String userName, String userPhone, String userEmail, int age, List<QuesAnswerVo> quesAnswerList,
			LocalDate fillinDate) {
		super();
		this.userName = userName;
		this.userPhone = userPhone;
		this.userEmail = userEmail;
		this.age = age;
		this.quesAnswerList = quesAnswerList;
		this.fillinDate = fillinDate;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public int getAge() {
		return age;
	}

	public List<QuesAnswerVo> getQuesAnswerList() {
		return quesAnswerList;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setQuesAnswerList(List<QuesAnswerVo> quesAnswerList) {
		this.quesAnswerList = quesAnswerList;
	}

	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}
	
}
