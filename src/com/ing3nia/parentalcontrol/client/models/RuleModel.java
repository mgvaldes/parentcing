package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;
import java.util.ArrayList;

public class RuleModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String keyId;
	
	private String startDate;
	
	private String endDate;
	
	private String name;
	
	private ArrayList<Integer> disabledFunctionalities;
	
	private String creationDate;
	
	private int type;

	public RuleModel() {
		super();
	}

	public RuleModel(String keyId, String startDate, String endDate,
			ArrayList<Integer> disabledFunctionalities, String name, int type) {
		super();
		this.keyId = keyId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.disabledFunctionalities = disabledFunctionalities;
		this.name = name;
		this.type = type;
	}
	
	public RuleModel(String startDate, String endDate,
			ArrayList<Integer> disabledFunctionalities, String name) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.disabledFunctionalities = disabledFunctionalities;
		this.name = name;
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
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
