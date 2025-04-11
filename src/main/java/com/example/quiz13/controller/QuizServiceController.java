package com.example.quiz13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.DeleteReq;
import com.example.quiz13.vo.GetQuestionRes;
import com.example.quiz13.vo.SearchReq;
import com.example.quiz13.vo.SearchRes;
import com.example.quiz13.vo.UpdateReq;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class QuizServiceController {
	
	
	@Autowired
	private QuizService quizService;
	
	@PostMapping(value = "quiz/create")
	public BasicRes create(@Valid @RequestBody CreateReq req) {
		return quizService.create(req);
	}
	
	@GetMapping(value = "quiz/getAll")
	public SearchRes getAll() {
		return quizService.getAll();
	}
	
	@PostMapping(value = "quiz/search")
	public SearchRes getAll(@RequestBody SearchReq req) {
		return quizService.getAll(req);
	}
	
	@PostMapping(value = "quiz/get_ques_by_quid_id")
	public GetQuestionRes getQuestionsByQuizId(@RequestParam(value = "quizId") int quizId) {
		return quizService.getQuestionsByQuizId(quizId);
	}
	
	@PostMapping(value = "quiz/update")
	public BasicRes update(@Valid @RequestBody UpdateReq req) {
		return quizService.update(req);
	}
	
	@Hidden
	@PostMapping(value = "quiz/delete")
	public BasicRes delete(@Valid @RequestBody DeleteReq req) {
		return quizService.delete(req);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
