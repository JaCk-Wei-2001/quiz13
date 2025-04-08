package com.example.quiz13.vo;

import java.util.List;

public class QuesIdAnswerVo {
	// 參數不用驗證，因為有可能這一題是簡答且非必填，加上驗證反而會出錯

	private int quesId;

	private List<String> answer;

	public QuesIdAnswerVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuesIdAnswerVo(int quesId, List<String> answer) {
		super();
		this.quesId = quesId;
		this.answer = answer;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public List<String> getAnswer() {
		return answer;
	}

	public void setAnswer(List<String> answer) {
		this.answer = answer;
	}

}
