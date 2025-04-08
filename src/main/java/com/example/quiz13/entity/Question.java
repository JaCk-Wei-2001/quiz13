package com.example.quiz13.entity;

import com.example.quiz13.constans.ResMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "question")
@IdClass(value = QuestionId.class)
public class Question {
	
	@Min(value = 1, message = ResMessage.ConstantsMessage.PARAM_QUES_ID_ERROR)
	@Id
	@Column(name = "ques_id")
	private int quesId;
	
	@Id
	@Column(name = "quiz_id")
	private int quizId;
	
	@NotBlank(message = ResMessage.ConstantsMessage.PARAM_QUES_NAME_ERROR)
	@Column(name = "ques_name")
	private String quesName;
	
	@NotBlank(message = ResMessage.ConstantsMessage.PARAM_QUES_TYPE_ERROR)
	@Column(name = "ques_type")
	private String quesType;

	@Column(name = "is_must")
	private boolean must;
	
	// 不用檢查，因為當 ques_type 是簡答題時 options 沒有選項
	@Column(name = "options")
	private String options;

	public Question() {
		super();
	}

	public Question(int quizId, int quesId, String quesName, String quesType, boolean must, String options) {
		super();
		this.quesId = quesId;
		this.quizId = quizId;
		this.quesName = quesName;
		this.quesType = quesType;
		this.must = must;
		this.options = options;
	}

	public int getQuesId() {
		return quesId;
	}

	public int getQuizId() {
		return quizId;
	}

	public String getQuesName() {
		return quesName;
	}

	public String getQuesType() {
		return quesType;
	}

	public boolean isMust() {
		return must;
	}

	public String getOptions() {
		return options;
	}

}
