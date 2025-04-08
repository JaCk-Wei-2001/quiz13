package com.example.quiz13;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.quiz13.dao.FeedbackDao;
import com.example.quiz13.dao.QuestionDao;
import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Question;
import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.FeedbackDto;
import com.example.quiz13.vo.SearchReq;
import com.example.quiz13.vo.SearchRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
class Quiz13ApplicationTests {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private FeedbackDao feedbackDao;

	@Autowired
	private QuizService quiz;

	@Test
	public void test() throws JsonProcessingException {

		String str = new ObjectMapper().writeValueAsString(List.of("AAA", "BBB", "CCC"));
		List<Question> list = new ArrayList<>();
		list.add(new Question( 3, 1,"A","single",true, str));
		LocalDate date = LocalDate.of(2025, 3, 27);
		LocalDate endDate = LocalDate.of(2025, 3, 28);
		CreateReq req = new CreateReq("AA","AAA",date,endDate,true,list);
		BasicRes res = quiz.create(req);
		System.out.println(res.getCode() + res.getMessage());
	}
	
	@Test
	public void test2() {
		SearchReq req = new SearchReq(null, null, null);
		SearchRes res = quiz.getAll(req);
		
		System.out.println(res.getQuizList().size());
	}
	
	
	@Test
	public void test3() {
		List<String> list = new ArrayList<>();
		List<String> strList = List.of("1:ABC", "2:adc", "3:CFE");
		for(String item: strList) {
			String[] strArray = item.split(":");
			list.addAll(List.of(strArray));
		}
		System.out.println(list.size());
	}
	
	@Test
	public void test4() {
		List<FeedbackDto> res = feedbackDao.selectFeedback(12);
		System.out.println(res.size());
	}
	
	
	
	
	
	
	
	
	
}
