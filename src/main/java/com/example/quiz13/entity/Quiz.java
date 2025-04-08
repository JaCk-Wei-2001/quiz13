package com.example.quiz13.entity;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.example.quiz13.constans.ResMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "quiz")
public class Quiz {
	
	@Id
	@Column(name = "quiz_id")
	private int quizId;
	
	@NotBlank(message = ResMessage.ConstantsMessage.PARAM_QUIZ_NAME_ERROR) 
	@Column(name = "quiz_name")
	private String quizName;
	
	@NotBlank(message = ResMessage.ConstantsMessage.PARAM_DESCRIPTION_ERROR)
	@Length(max = 200, message = ResMessage.ConstantsMessage.PARAM_DESCRIPTION_LENGTH_TOO_LONG)
	@Column(name = "description")
	private String description;
	
	@NotNull(message = ResMessage.ConstantsMessage.PARAM_START_DATE_ERROR)
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@NotNull(message = ResMessage.ConstantsMessage.PARAM_END_DATE_ERROR)
	@Column(name = "end_date")
	private LocalDate endDate;
	
	@Column(name = "is_published")
	private boolean publish;

	public Quiz() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Quiz(String quizName, String description, LocalDate startDate, LocalDate endDate, boolean publish) {
		super();
		this.quizName = quizName;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.publish = publish;
	}

	public int getQuizId() {
		return quizId;
	}

	public String getQuizName() {
		return quizName;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public boolean isPublish() {
		return publish;
	}

}
