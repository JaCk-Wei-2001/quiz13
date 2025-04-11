package com.example.quiz13.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz13.constans.QuesType;
import com.example.quiz13.constans.ResMessage;
import com.example.quiz13.dao.FeedbackDao;
import com.example.quiz13.dao.QuestionDao;
import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Feedback;
import com.example.quiz13.entity.Question;
import com.example.quiz13.service.ifs.FeedbackService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.FeedbackDto;
import com.example.quiz13.vo.FeedbackRes;
import com.example.quiz13.vo.FeedbackVo;
import com.example.quiz13.vo.FillinReq;
import com.example.quiz13.vo.GetQuestionRes;
import com.example.quiz13.vo.OptionCountVo;
import com.example.quiz13.vo.QuesAnswerVo;
import com.example.quiz13.vo.QuesIdAnswerVo;
import com.example.quiz13.vo.StatisticsRes;
import com.example.quiz13.vo.StatisticsVo;
import com.example.quiz13.vo.UserRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private FeedbackDao feedbackDao;

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	@Override
	public BasicRes fillin(FillinReq req) {

		// 1. 檢查 quiz
		// 1.1 是否已發布以及當前時間是否介於開始時間和結束時間內
		int count = quizDao.selectCountByQuizId(req.getQuizId(), LocalDate.now());

		if (count != 1) {
			return new BasicRes(ResMessage.OUT_OF_FILLIN_DATE_RANGE.getCode(), //
					ResMessage.OUT_OF_FILLIN_DATE_RANGE.getMessage());
		}

		// 2. 檢查feedback data
		// 同一個 email 只能填寫一張問卷一次
		count = feedbackDao.selectCount(req.getQuizId(), req.getUserEmail());

		if (count != 0) { // 0表示同一組email 沒有填寫過 同一張問卷
			return new BasicRes(ResMessage.EMAIL_DUPLICATED.getCode(), //
					ResMessage.EMAIL_DUPLICATED.getMessage());
		}

		// 3. 檢查答案
		List<QuesIdAnswerVo> quesIdanswerList = req.getQuesIdanswerList();
		// 3.1 從DB撈問題
		List<Question> questionList = questionDao.getByQuizId(req.getQuizId());
		// 3.2 比對問題選項與答案

		for (Question quesItem : questionList) {
			int quesId = quesItem.getQuesId();
			String type = quesItem.getQuesType();
			boolean must = quesItem.isMust();

			List<String> optionsList = new ArrayList<>();
			try {
				// 只轉換 type 非 text 因為 text 沒有選項轉換
				if (!type.equalsIgnoreCase(QuesType.TEXT.getType())) {
					optionsList = mapper.readValue(quesItem.getOptions(), new TypeReference<>() {
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new BasicRes(ResMessage.OPTIONS_PARSE_ERROR.getCode(), //
						ResMessage.OPTIONS_PARSE_ERROR.getMessage());
			}
			for (QuesIdAnswerVo voItem : quesIdanswerList) {

				List<String> answerList = new ArrayList<>();
				if (quesId == voItem.getQuesId()) {
					answerList = voItem.getAnswer();
					// 經過上一段的 if 判斷式，answerList 可能會有答案，也可能沒有
					// 排除: 必填但沒有答案
					if (must && answerList.isEmpty()) { // must等同於 must == true
						return new BasicRes(ResMessage.ANSWER_REQUIRED.getCode(), //
								ResMessage.ANSWER_REQUIRED.getMessage());
					}
					// 跳過 type 是 text 的，因為沒有選項可以比對
					if (type.equalsIgnoreCase(QuesType.TEXT.getType())) {
						continue;
					}
					// 單選時答案不能有多個
					if (type.equalsIgnoreCase(QuesType.SINGLE.getType()) && answerList.size() > 1) {
						return new BasicRes(ResMessage.ONE_OPTION_IS_ALLOWED.getCode(), //
								ResMessage.ONE_OPTION_IS_ALLOWED.getMessage());
					}
					// 比對同一題的答案是否都在選項中
					boolean checkRes = checkAnswer(optionsList, answerList);
					if (!checkRes) {
						return new BasicRes(ResMessage.ANSWER_OPTIONS_MISMATCH.getCode(), //
								ResMessage.ANSWER_OPTIONS_MISMATCH.getMessage());
					}
				}

			}
		}
		// 存資料到 DB

		for (QuesIdAnswerVo voItem : quesIdanswerList) {

			try {
				String answerStr = mapper.writeValueAsString(voItem.getAnswer());
				feedbackDao.insert(//
						req.getQuizId(), voItem.getQuesId(), req.getUserName(), req.getUserPhone(), //
						req.getUserEmail(), req.getUserAge(), answerStr);
			} catch (JsonProcessingException e) {
				return new BasicRes(ResMessage.ANSWER_PARSE_ERROR.getCode(), //
						ResMessage.ANSWER_PARSE_ERROR.getMessage());
			} catch (Exception e) {
				throw e;
			}

		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	private boolean checkAnswer(List<String> optionsList, List<String> answerList) {
		// 切串接的字串
		List<String> newOptionList = new ArrayList<>();
		for (String item : optionsList) {
			String[] strArray = item.split(":");
			newOptionList.addAll(List.of(strArray));
		}
		// 比對選項和答案
		for (String ans : answerList) {
			// 假設一種情況，optionsList = ["A", "B", "C", "D"], answerList = ["A", "a"]
			// 若下方的 if 是沒有驚嘆號的判斷式，if(optionsList.contains(answer))，且回傳true
			// 當 answerList 中第一個答案 A 判斷後就會回傳true ，後面的 a 就會沒判斷到
			if (!optionsList.contains(ans)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public FeedbackRes feedback(int quizId) {

		// 檢查參數
		if (quizId <= 0) {
			return new FeedbackRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), //
					ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		// 撈資料
		List<FeedbackDto> res = feedbackDao.selectFeedback(quizId);
		// 整理資料: 透過相同的email 把問題和回答整理在一起
		// Map<String, List<QuesAnswerVo>> 中的 key 是放 email
		Map<String, List<QuesAnswerVo>> map = new HashMap<>();
		String quizName = "";
		String description = "";

		for (FeedbackDto dto : res) {
			quizName = dto.getQuizName();
			description = dto.getDescription();

			String email = dto.getUserEmail();
			List<QuesAnswerVo> voList = new ArrayList<>();

			if (map.containsKey(email)) {
				// 若 map 中已存在相同的 key(email)，就從 map 中把對應的 value 取出
				voList = map.get(email);
			}
			QuesAnswerVo vo = new QuesAnswerVo();
			vo.setQuesId(dto.getQuesId());
			vo.setQuesName(dto.getQuesName());
			try {
				// 將DB 中的 answer String 轉成 List<String>
				List<String> answerList = mapper.readValue(dto.getAnswer(), new TypeReference<>() {
				});
				vo.setAnswer(answerList);
			} catch (Exception e) {
				return new FeedbackRes(ResMessage.ANSWER_PARSE_ERROR.getCode(), //
						ResMessage.ANSWER_PARSE_ERROR.getMessage());
			}
			voList.add(vo);
			map.put(email, voList);
		}

		List<FeedbackVo> feedbackList = new ArrayList<>();
		a: for (FeedbackDto dto : res) {
			String email = dto.getUserEmail();
			for (FeedbackVo vo : feedbackList) {
				if (email.equalsIgnoreCase(vo.getUserEmail())) {
					continue a;
				}
			}
			FeedbackVo feedbackVo = new FeedbackVo();
			feedbackVo.setUserName(dto.getUserName());
			feedbackVo.setUserPhone(dto.getUserPhone());
			feedbackVo.setUserEmail(dto.getUserEmail());
			feedbackVo.setAge(dto.getAge());
			feedbackVo.setFillinDate(dto.getFillinDate());
			feedbackVo.setQuesAnswerList(map.get(dto.getUserEmail()));
			feedbackList.add(feedbackVo);
		}

		return new FeedbackRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), quizName, description, feedbackList);
	}

	@Override
	public StatisticsRes statistics(int quizId) {
		// 從 question 找選項，不直接從答案找主要是要預防可能會有某個選項都沒人選
		List<Question> questionList = questionDao.getByQuizId(quizId);
		List<StatisticsVo> statisticsVoList = new ArrayList<>();

		// Map<問題編號, 多個選項>
		Map<Integer, List<String>> quesIdOptionsMap = new HashMap<>();

		for (Question item : questionList) {
			// 將 DB 中的 question 相關資訊設定到 StatisticsVo 中，先排除統計
			StatisticsVo vo = new StatisticsVo(item.getQuesId(), item.getQuesName(), //
					item.getQuesType(), item.isMust());
			statisticsVoList.add(vo);

			// 如果是非 TEXT 題型（選擇題），解析選項字串為 List
			if (!item.getQuesType().equalsIgnoreCase(QuesType.TEXT.getType())) {
				try {
					List<String> optionList = mapper.readValue(item.getOptions(), new TypeReference<>() {});
					quesIdOptionsMap.put(item.getQuesId(), optionList);
				} catch (Exception e) {
					return new StatisticsRes(ResMessage.OPTIONS_PARSE_ERROR.getCode(), //
							ResMessage.OPTIONS_PARSE_ERROR.getMessage());
				}
			} else {
				// TEXT 題型設定為 null（不需要選項統計）
				quesIdOptionsMap.put(item.getQuesId(), null);
			}
		}

		// 撈出所有回答資料
		Map<Integer, List<String>> quesIdAnswersMap = getAnswers(quizId);
		if (quesIdAnswersMap == null) {
			return new StatisticsRes(ResMessage.ANSWER_PARSE_ERROR.getCode(), //
					ResMessage.ANSWER_PARSE_ERROR.getMessage());
		}

		// 統計各選項被選的次數
		Map<Integer, List<OptionCountVo>> map = computeAnswerCount(quesIdOptionsMap, quesIdAnswersMap);

		for (StatisticsVo vo : statisticsVoList) {
			int quesId = vo.getQuesId();

			// 設定選項統計結果（僅針對選擇題）
			vo.setOptionCountVoList(map.get(quesId));

			// 如果是簡答題，整合所有答案為單一字串
			if (QuesType.TEXT.getType().equalsIgnoreCase(vo.getQuesType())) {
				List<String> answers = quesIdAnswersMap.get(quesId);
				if (answers != null && !answers.isEmpty()) {
					String allAnswers = String.join("\n", answers); // 換行分隔
					vo.setTextAnswer(allAnswers);
				} else {
					vo.setTextAnswer(""); // 沒有答案也給空字串
				}
			}
		}

		return new StatisticsRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), statisticsVoList);
	}


	private Map<Integer, List<OptionCountVo>> computeAnswerCount(Map<Integer, List<String>> quesIdOptionsMap, //
			Map<Integer, List<String>> quesIdAnswersMap) {
		// Map<問題編號, List<OptionCountVo>>
		Map<Integer, List<OptionCountVo>> map = new HashMap<>();
		for (Entry<Integer, List<String>> mapItem : quesIdOptionsMap.entrySet()) {
			List<OptionCountVo> optionCountVoList = new ArrayList<>();
			int quesId = mapItem.getKey();
			List<String> optionsList = mapItem.getValue();
			// 答案包含了: 1. 單選的選項 2. 多選的選項 3. 簡答內容
			if (quesIdAnswersMap.containsKey(quesId) && optionsList != null) {
				List<String> answersList = quesIdAnswersMap.get(quesId);
				int size = answersList.size();
				for (String option : optionsList) {
					answersList.removeAll(List.of(option));
					int optionSize = size - answersList.size();
					OptionCountVo vo = new OptionCountVo(option, optionSize);
					// 改變 answersList size
					size = size - optionSize;
					optionCountVoList.add(vo);
				}
			}
			map.put(quesId, optionCountVoList);
		}
		return map;
	}

	private Map<Integer, List<String>> getAnswers(int quizId) {
		// 取答案
		List<Feedback> feedbackList = feedbackDao.selectByQuizId(quizId);
		// Map<問題編號, 多個答案>>
		Map<Integer, List<String>> quesIdAnswersMap = new HashMap<>();
		for (Feedback item : feedbackList) {
			if (StringUtils.hasText(item.getAnswer())) {
				int quesId = item.getQuesId();
				// 將List<String>轉換成List
				try {
					List<String> answersList = mapper.readValue(item.getAnswer(), new TypeReference<>() {
					});
					if (quesIdAnswersMap.containsKey(quesId)) {
						// 取出已存在 map 中的 answer List
						List<String> list = quesIdAnswersMap.get(quesId);
						// 把兩個 List 相加
						list.addAll(answersList);
						// 把結果放回到 map 中
						quesIdAnswersMap.put(quesId, list);
					} else {
						quesIdAnswersMap.put(quesId, answersList);
					}

				} catch (Exception e) {
					return null;

				}
			}
		}
		return quesIdAnswersMap;
	}

	
	
	@Override
	public UserRes selectByQuizId(int quizId) {
		if (quizId <= 0) {
			return new UserRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), //
					ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		List<Feedback> list = feedbackDao.selectByQuizId(quizId);
		return new UserRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), list);
	}

}
