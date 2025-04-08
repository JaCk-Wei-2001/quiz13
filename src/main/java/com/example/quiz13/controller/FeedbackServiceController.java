package com.example.quiz13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz13.service.ifs.FeedbackService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.FillinReq;

import jakarta.validation.Valid;


@CrossOrigin
@RestController
public class FeedbackServiceController {
	
	
	@Autowired
	private FeedbackService feedbackService;
	
	@PostMapping(value = "feedback/fillin")
	public BasicRes fillin(@Valid @RequestBody FillinReq req) {
		return feedbackService.fillin(req);
	}
}
