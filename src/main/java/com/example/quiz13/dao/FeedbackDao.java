package com.example.quiz13.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.quiz13.entity.Feedback;
import com.example.quiz13.entity.FeedbackId;
import com.example.quiz13.vo.FeedbackDto;

import jakarta.transaction.Transactional;

public interface FeedbackDao extends JpaRepository<Feedback, FeedbackId> {

	@Query(value = "select count(quiz_id) from quiz13.feedback where quiz_Id = ?1 "
			+ "and user_email = ?2", nativeQuery = true)
	public int selectCount(int quizId, String userEmail);

	@Modifying
	@Transactional
	@Query(value = "insert into feedback "
			+ "(user_name, user_email, user_phone, user_age, quiz_id, ques_id, answer, fillin_date) "
			+ " values (:userName, :userEmail, :userPhone, :userAge, :quizId, :quesId, :answer, now())" + "", nativeQuery = true)
	public void insert(//
			@Param("quizId") int quizId, //
			@Param("quesId") int quesId, //
			@Param("userName") String userName, //
			@Param("userPhone") String userPhone, //
			@Param("userEmail") String userEmail, //
			@Param("userAge") int userAge, //
			@Param("answer") String answer);

	/*
	 * 1. join 會撈多張表，所以裝資料的容器無法只用 entity 來裝載，只能創建新的容器(Dto)</br>
	 * 2. 新建立的 Dto 沒有被 spring boot 託管(在類別上加上 Annotation)，所以要透過 new  來新建，且要有完整的路徑</br>
	 * 3. 因為裝資料的容器是跨多張表的 Dto ，所以 nativeQuery 就得要變成 false</br>
	 * 4. nativeQuery = false 時，SQL語法中，表的名字要變成Entity class 的名稱，欄位名稱就會是屬性變數名稱
	 */
	@Query(value = "select new com.example.quiz13.vo.FeedbackDto("
			+ " QZ.quizId, QZ.quizName, QZ.description, "
			+ " FB.userName, FB.userPhone, FB.userEmail, FB.userAge, "
			+ " QT.quesId, QT.quesName, FB.answer, FB.fillinDate) "
			+ " from Quiz as QZ  join Question as QT on QZ.quizId = QT.quizId "
			+ " join Feedback as FB on QZ.quizId = FB.quizId "
			+ " where QZ.id = ?1 and QT.quesId = FB.quesId", nativeQuery = false)
	public List<FeedbackDto> selectFeedback(int quizId);
	
	@Query(value = "select * from quiz13.feedback where quiz_id = ?", nativeQuery = true)
	public List<Feedback> selectByQuizId(int quizId);
}
