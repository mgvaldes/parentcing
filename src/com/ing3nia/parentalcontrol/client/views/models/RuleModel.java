package com.ing3nia.parentalcontrol.client.views.models;

import java.util.Date;

public class RuleModel {
	/**
	 * Start date of the rule.
	 */
	private Date startDate;
	
	/**
	 * End date of the rule.
	 */
	private Date endDate;
	
	/**
	 * Name of the device associated to this alert.
	 */
	private String rule;
	
	public RuleModel(Date startDate, Date endDate, String rule) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.rule = rule;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}
}
