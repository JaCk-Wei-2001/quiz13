package com.example.quiz13.vo;

import java.util.List;

import com.example.quiz13.entity.Feedback;



public class UserRes extends BasicRes{

	private List<Feedback> userMessage;

	public UserRes() {
		super();
	}

	public UserRes(int code, String message) {
		super(code, message);
	}

	public UserRes(int code, String message, List<Feedback> userMessage) {
		super(code, message);
		this.userMessage = userMessage;
	}

	public List<Feedback> getUserMessage() {
		return userMessage;
	}

	
	
	
}
