package com.example.quiz13.vo;

import java.util.List;

public class StatisticsVo {

	private int quesId;

	private String quesName;

	private String quesType;

	private boolean must;

	private List<OptionCountVo> optionCountVoList;

	public StatisticsVo() {
		super();
	}

	public StatisticsVo(int quesId, String quesName, String quesType, boolean must,
			List<OptionCountVo> optionCountVoList) {
		super();
		this.quesId = quesId;
		this.quesName = quesName;
		this.quesType = quesType;
		this.must = must;
		this.optionCountVoList = optionCountVoList;
	}

	public StatisticsVo(int quesId, String quesName, String quesType, boolean must) {
		super();
		this.quesId = quesId;
		this.quesName = quesName;
		this.quesType = quesType;
		this.must = must;
	}

	public int getQuesId() {
		return quesId;
	}

	public String getQuesName() {
		return quesName;
	}

	public String getQuesType() {
		return quesType;
	}

	public boolean isMust() {
		return must;
	}

	public List<OptionCountVo> getOptionCountVoList() {
		return optionCountVoList;
	}

	public void setOptionCountVoList(List<OptionCountVo> optionCountVoList) {
		this.optionCountVoList = optionCountVoList;
	}

}
