package com.example.quiz13.service.ifs;


import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.FeedbackRes;
import com.example.quiz13.vo.FillinReq;
import com.example.quiz13.vo.StatisticsRes;
import com.example.quiz13.vo.UserRes;

public interface FeedbackService {

	
	public BasicRes fillin(FillinReq req);
	
	public FeedbackRes feedback(int quizId);
	
	public StatisticsRes statistics(int quizId);
	
	public UserRes selectByQuizId(int quizId);
}
