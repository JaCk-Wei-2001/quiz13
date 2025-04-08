package com.example.quiz13.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.quiz13.constans.QuesType;
import com.example.quiz13.constans.ResMessage;
import com.example.quiz13.dao.QuestionDao;
import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Question;
import com.example.quiz13.entity.Quiz;
import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.DeleteReq;
import com.example.quiz13.vo.GetQuestionRes;
import com.example.quiz13.vo.SearchReq;
import com.example.quiz13.vo.SearchRes;
import com.example.quiz13.vo.UpdateReq;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	// @Transactional 只有錯誤發生在 RuntimeException(或其子類別)時才會將資料回逤
	// rollbackFor = Exception.class 是將資料回逤指定到只要發生 Exception 時，就會將資料回逤
	// 因為 Exception 是所有 xxxException 的父類別，只要發生 xxxException 時，資料回逤都會生效
	// rollbackFor : import 的 library 是
	// org.springframework.transaction.annotation.Transactional;
	// rollbackon : jakarta.transaction.Transactional
	@Transactional(rollbackFor = Exception.class)
	@Override
	public BasicRes create(CreateReq req) {
		// 參數檢查: req 的參數已透過 @Valid 做基本的檢查
		// 開始時間不能比結束時間晚
		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessage.PARAM_DATE_ERROR.getCode(), //
					ResMessage.PARAM_DATE_ERROR.getMessage());
		}
		// 檢查問題內容
		BasicRes checkRes = checkQuestions(req.getQuestionList());
		if (checkRes != null) {
			return checkRes;
		}
		try {
			// 將資料寫進 DB
			// 將資料寫進 quiz
			quizDao.insert(req.getQuizName(), req.getDescription(), req.getStartDate(), //
					req.getEndDate(), req.isPublish());
			// 取得最新一筆quiz_id
			int quizId = quizDao.selectMaxQuizId();
			// 將資料寫進 question
			for (Question item : req.getQuestionList()) {
				questionDao.insert(quizId, item.getQuesId(), item.getQuesName(), //
						item.getQuesType(), item.isMust(), item.getOptions());
			}
		} catch (Exception e) {
			// 錯誤要 throw 出去 ，@Transactional 才會生效(將資料回朔)
			throw e;

		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	private BasicRes checkQuestions(List<Question> list) {
		for (Question item : list) {
			// 檢查 question type
			if (!QuesType.checkType(item.getQuesType())) {
				return new BasicRes(ResMessage.QUES_TYPE_ERROR.getCode(), //
						ResMessage.QUES_TYPE_ERROR.getMessage());
			}
			// 排除 : type 不是 text 的時候 並且 選項沒有值
			if (!item.getQuesType().equalsIgnoreCase(QuesType.TEXT.getType()) && //
					!StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(), //
						ResMessage.PARAM_OPTIONS_ERROR.getMessage());
			}
			// 排除 : type 是 text 的時候 並且 選項有值
			if (item.getQuesType().equalsIgnoreCase(QuesType.TEXT.getType()) && //
					StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(), //
						ResMessage.PARAM_OPTIONS_ERROR.getMessage());
			}
			// 問卷的 type = text，options 沒資料，所以不用執行後續

			// 非簡答，選項至少有2個
			// 將選項字串轉成 List<String>: 要先確定當初創建問卷時，前端的多個選項是陣列，且使用 Stringify 轉成字串型態
			// 前端選項原本格式(陣列): ["A.aa","B.bb", "C.cc"]
			ObjectMapper mapper = new ObjectMapper();
			try {
				if(!item.getQuesType().equalsIgnoreCase(QuesType.TEXT.getType())) {
					List<String> optionsList = mapper.readValue(item.getOptions(), new TypeReference<>() {
					});
					if (item.getQuesType().equalsIgnoreCase(QuesType.SINGLE.getType()) || //
							item.getQuesType().equalsIgnoreCase(QuesType.MULTI.getType())) {
						if (optionsList.size() < 2) {
							return new BasicRes(ResMessage.OPTIONS_SIZE_ERROR.getCode(), //
									ResMessage.OPTIONS_SIZE_ERROR.getMessage());
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return new BasicRes(ResMessage.OPTIONS_PARSE_ERROR.getCode(), //
						ResMessage.OPTIONS_PARSE_ERROR.getMessage());
			}

		}
		return null;
	}

	@Override
	public SearchRes getAll() {
		List<Quiz> list = quizDao.getAll();
		return new SearchRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), //
				list);
	}

	@Override
	public GetQuestionRes getQuestionsByQuizId(int quizId) {
		if (quizId <= 0) {
			return new GetQuestionRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), //
					ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		List<Question> list = questionDao.getByQuizId(quizId);
		return new GetQuestionRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), list);
	}

	// 因為 update 的方法包含了跨表超做，所以要將這些 Dao 的操作當作同一次的行為
	@Transactional(rollbackFor = Exception.class)
	@Override
	public BasicRes update(UpdateReq req) {
		// 不用檢查 quiz 的 id 是否存在，因為最後的更新語法中條件是 quiz 的 id，不存在也不會更新成功
		// 要檢查 quiz 的 id 是否存在，因為最後是先將問題的資料刪除後再新增，而不是用更新語法
		// 不使用更新語法的考量原因是，更新前有5題，更新後變成4題，若是用更新語法，原本的第5題還會存在
		// 因 quiz 的 id 不存在，問題刪除時資料不會變更，但接著新增資料時就會把這些資料沒有對應的 quiz_id
		int count = quizDao.selectCountByQuizId(req.getQuizId());
		if (count != 1 ) {
			return new BasicRes(ResMessage.ID_NOT_FOUND.getCode(), //
					ResMessage.ID_NOT_FOUND.getMessage());
		}
		// 開始時間不能比結束時間晚
		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessage.PARAM_DATE_ERROR.getCode(), //
					ResMessage.PARAM_DATE_ERROR.getMessage());
		}
		// 檢查問題內容
		BasicRes checkRes = checkQuestions(req.getQuestionList());
		if (checkRes != null) {
			return checkRes;
		}
		// 檢查Question 中的 quizId 是否與 Quiz 中的 id 是否一樣
		for (Question item : req.getQuestionList()) {
			if (item.getQuizId() != req.getQuizId()) {
				return new BasicRes(ResMessage.QUIZ_ID_MISMATCK.getCode(), //
						ResMessage.QUIZ_ID_MISMATCK.getMessage());
			}
		}

		try {
			// 1.更新問卷
			quizDao.updateByQuizId(req.getQuizId(), req.getQuizName(), req.getDescription(), //
					req.getStartDate(), req.getEndDate(), req.isPublish());
			// 2.刪除同張問卷的所有問題
			questionDao.deleteByQuizId(req.getQuizId());
			// 3.新增問題
			for (Question item : req.getQuestionList()) {
				questionDao.insert(item.getQuizId(), item.getQuesId(), item.getQuesName(), //
						item.getQuesType(), item.isMust(), item.getOptions());
			}
		} catch (Exception e) {

		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
		
	}

	@Override
	public SearchRes getAll(SearchReq req) {
		// 轉換值
		String quizName = req.getQuizName();
		LocalDate startDate = req.getStartDate();
		LocalDate endDate = req.getEndDate();
		
		if(!StringUtils.hasText(quizName)) {
			// 將 quizName = null 或是 quizName = 全空白字串，轉換成空字串
			quizName = "";
		}
		if(startDate == null) {
			// start 沒有值的話表示不是條件之一所以帶入 localdate 最小值
			startDate = LocalDate.of(1970, 1, 1);
		}
		if(endDate == null) {
			// end 沒有值的話表示不是條件之一所以帶入 localdate 較大值
			endDate = LocalDate.of(2999, 1, 1);
		}
		List<Quiz> list = quizDao.getAll(quizName, startDate, endDate);
		
		return new SearchRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), list);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BasicRes delete(DeleteReq req) {
		
		try {
			quizDao.delete(req.getQuizIdList());
			questionDao.delete(req.getQuizIdList());
		} catch (Exception e) {
			throw e;
		}
		
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

}
