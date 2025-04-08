package com.example.quiz13.vo;

import java.util.List;

public class StatisticsRes extends BasicRes {

	private List<StatisticsVo> statistics;
	

	public StatisticsRes() {
		super();
	}

	
	public StatisticsRes(int code, String message, List<StatisticsVo> statistics) {
		super(code, message);
		this.statistics = statistics;
	}

	public StatisticsRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}


	public List<StatisticsVo> getStatistics() {
		return statistics;
	}

	
}
