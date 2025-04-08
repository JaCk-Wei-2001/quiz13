package com.example.quiz13.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.quiz13.entity.Quiz;

import jakarta.transaction.Transactional;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer> {

	// SQL 語法中 ":name" 對應的變數名稱是 @Param("name") 中的字串，其餘皆同
	@Modifying
	@Transactional
	@Query(value = "insert into quiz (quiz_name, description, start_date, end_date, is_published) "
			+ "values (:name, :description, :startDate, :endDate, :published)", nativeQuery = true)
	public void insert( //
			@Param("name") String name, //
			@Param("description") String description, //
			@Param("startDate") LocalDate startDate, //
			@Param("endDate") LocalDate endDate, //
			@Param("published") boolean published);

	@Query(value = "select max(quiz_id) from quiz13.quiz", nativeQuery = true)
	public int selectMaxQuizId();

	@Query(value = "select * from quiz13.quiz", nativeQuery = true)
	public List<Quiz> getAll();

	@Query(value = "select * from quiz13.quiz "
			+ "where quiz_name like %?1% and start_date >= ?2 and end_date <= ?3", nativeQuery = true)
	public List<Quiz> getAll(String quizName, LocalDate starDate, LocalDate endDate);

	@Query(value = "select count(quiz_id) from quiz13.quiz where quiz_id = ?", nativeQuery = true)
	public int selectCountByQuizId(int quizId);
	
	
	@Modifying
	@Transactional
	@Query(value = "update quiz set name = :name, " + "description = :description, " + "start_date = :startDate, "
			+ "end_date = :endDate, " + "is_published = :published " + "where id = :quizId", nativeQuery = true)
	public void updateByQuizId(//
			@Param("quizId") int quizId, //
			@Param("name") String name, //
			@Param("description") String description, //
			@Param("startDate") LocalDate startDate, //
			@Param("endDate") LocalDate endDate, //
			@Param("published") boolean published);

	@Modifying
	@Transactional
	@Query(value = "delete from quiz13.quiz where quiz_id in (?)", nativeQuery = true)
	public void delete(List<Integer> quizIdList);
	
	
	@Query(value = "select count(quiz_id) from quiz13.quiz where quiz_id = ?1 and ?2 >= start_date "
			+ " and ?2 <= end_date and is_published  = true", nativeQuery = true)
	public int selectCountByQuizId(int quizId, LocalDate now);  //*
	
	
	
	
	
	
	
	
	
	
}
