package com.example.quiz13.constans;

public enum ResMessage {
	
	SUCCESS(200, "Success!!"), //
	PARAM_DATE_ERROR(400, "Param date error!!"), //
	QUES_TYPE_ERROR(400, "Ques type error!!"), //
	PARAM_OPTIONS_ERROR(400, "Param options error!!"), //
	OPTIONS_PARSE_ERROR(400, "Options parse error!!"), //
	OPTIONS_SIZE_ERROR(400, "Options size error!!"), //
	SQL_ERROR(400, "SQL error!!"), //
	PARAM_QUIZ_ID_ERROR(400, "Param quiz id error!!"), //
	QUIZ_ID_MISMATCK(400, "Quiz id mismatch!!"), //
	ID_NOT_FOUND(400, "Id not found!!"), //
	OUT_OF_FILLIN_DATE_RANGE(400, "Out of fillin date range!!"), //
	EMAIL_DUPLICATED(400, "Email duplicated!!"), //
	ANSWER_REQUIRED(400, "Answer required!!"), //
	ANSWER_OPTIONS_MISMATCH(400, "Answer options mismatch!!"), //
	ONE_OPTION_IS_ALLOWED(400, "One option is allowed!!"), //
	ANSWER_PARSE_ERROR(400, "Answer parse error!!"), //


	;
	
	private int code;
	
	private String message;

	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public static class ConstantsMessage {
		public static final String PARAM_QUIZ_NAME_ERROR = "Param quiz name error!!";
		public static final String PARAM_DESCRIPTION_ERROR = "Param description error!!";
		public static final String PARAM_DESCRIPTION_LENGTH_TOO_LONG = "Param description too long!!";
		public static final String PARAM_START_DATE_ERROR = "Param start_Date error!!";
		public static final String PARAM_END_DATE_ERROR = "Param end_date error!!";
		
		public static final String PARAM_QUIZ_ID_ERROR = "Param quiz_id error!!";
		public static final String PARAM_QUES_ID_ERROR = "Param ques_id error!!";
		
		public static final String PARAM_QUES_NAME_ERROR = "Param question name error!!"; 
		public static final String PARAM_QUES_TYPE_ERROR = "Param question type error!!"; 
		
		public static final String PARAM_QUES_LIST_ERROR = "Param question list error!!"; 
		
		public static final String PARAM_ACCOUNT_ERROR = "Param admin account error!!";
		public static final String PARAM_PASSWORD_ERROR = "Param admin password error!!";
		
		public static final String PARAM_QUES_ID_LIST_ERROR = "Param question id list error!!"; 
		
		public static final String PARAM_USER_EMAIL_ERROR = "Param user email is error!!"; 
		
	}
	
}
