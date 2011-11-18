package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

public class ClientRuleModel {
	private String key;
	
	private String startDate;
	
	private String endDate;
	
	private ArrayList<Integer> disabledFunctionalities;
	
	private String creationDate;

	public ClientRuleModel() {
		super();
	}

	public ClientRuleModel(String key, String startDate, String endDate,
			ArrayList<Integer> disabledFunctionalities) {
		super();
		this.key = key;
		this.startDate = startDate;
		this.endDate = endDate;
		this.disabledFunctionalities = disabledFunctionalities;
	}
	
	public ClientRuleModel(String startDate, String endDate,
			ArrayList<Integer> disabledFunctionalities) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.disabledFunctionalities = disabledFunctionalities;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public ArrayList<Integer> getDisabledFunctionalities() {
		return disabledFunctionalities;
	}

	public void setDisabledFunctionalities(
			ArrayList<Integer> disabledFunctionalities) {
		this.disabledFunctionalities = disabledFunctionalities;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	

	public String getCreationDate() {
		return creationDate;
	}
}
