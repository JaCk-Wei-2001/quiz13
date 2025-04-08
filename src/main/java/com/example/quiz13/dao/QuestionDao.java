package com.example.quiz13.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.quiz13.entity.Question;
import com.example.quiz13.entity.QuestionId;

import jakarta.transaction.Transactional;

public interface QuestionDao extends JpaRepository<Question, QuestionId> {

	// SQL 語法中 ":name" 對應的變數名稱是 @Param("name") 中的字串，其餘皆同
	@Modifying
	@Transactional
	@Query(value = "insert into question (quiz_id, ques_id, ques_name, ques_type, is_must, options) "
			+ "values (:quizId, :quesId, :quesName, :quesType, :must, :options)", nativeQuery = true)
	public void insert( //
			@Param("quizId") int quizId, //
			@Param("quesId") int quesId, //
			@Param("quesName") String quesName, //
			@Param("quesType") String quesType, //
			@Param("must") boolean must, //
			@Param("options") String options);
	
	@Query(value = "select * from quiz13.question where quiz_id = ?1", nativeQuery = true)
	public List<Question> getByQuizId(int quizId); //*
	
	@Modifying
	@Transactional
	@Query(value = "delete from quiz13.question where quiz_id = ?", nativeQuery = true)
	public void deleteByQuizId(int quizId);
	
	@Modifying
	@Transactional
	@Query(value = "delete from quiz13.question where quiz_id in (?)", nativeQuery = true)
	public void delete(List<Integer> quizIdList);
}
