package com.example.quiz13.vo;

import java.util.List;

import com.example.quiz13.constans.ResMessage;

import jakarta.validation.constraints.*;

public class FillinReq {
	
	@Min(value = 1, message = ResMessage.ConstantsMessage.PARAM_QUIZ_ID_ERROR)
	private int quizId;

	private String userName;

	private String userPhone;
	
	@NotBlank(message = ResMessage.ConstantsMessage.PARAM_USER_EMAIL_ERROR)
	private String userEmail;

	private int userAge;
	
	// 此參數不用驗證，一個極端的情況是所有問題都是非必填且都沒有作答
	private List<QuesIdAnswerVo> quesIdanswerList;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getUserAge() {
		return userAge;
	}

	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}

	

	public List<QuesIdAnswerVo> getQuesIdanswerList() {
		return quesIdanswerList;
	}

	public void setQuesIdanswerList(List<QuesIdAnswerVo> quesIdanswerList) {
		this.quesIdanswerList = quesIdanswerList;
	}

}
