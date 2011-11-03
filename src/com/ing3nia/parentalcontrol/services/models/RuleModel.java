package com.ing3nia.parentalcontrol.services.models;

import java.util.ArrayList;
import java.util.Date;

import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCRule;

public class RuleModel {
	private Date startDate;
	
	private Date endDate;
	
	private ArrayList<FunctionalityModel> disabledFunctionalities;

	private Date creationDate;

	public RuleModel() {
		super();
	}

	public RuleModel(Date startDate, Date endDate,
			ArrayList<FunctionalityModel> disabledFunctionalities,
			Date creationDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.disabledFunctionalities = disabledFunctionalities;
		this.creationDate = creationDate;
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

	public ArrayList<FunctionalityModel> getDisabledFunctionalities() {
		return disabledFunctionalities;
	}

	public void setDisabledFunctionalities(
			ArrayList<FunctionalityModel> disabledFunctionalities) {
		this.disabledFunctionalities = disabledFunctionalities;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public static RuleModel convertToRuleModel(PCRule rule) {
		RuleModel ruleModel = new RuleModel();
		
		ruleModel.setStartDate(rule.getStartDate());
		ruleModel.setEndDate(rule.getEndDate());
		
		ArrayList<FunctionalityModel> disabledFunctionalityModels = new ArrayList<FunctionalityModel>();
		
		for (PCFunctionality functionality : rule.getDisabledFunctionalities()) {
			disabledFunctionalityModels.add(FunctionalityModel.convertToFunctionalityModel(functionality));
		}
		
		ruleModel.setDisabledFunctionalities(disabledFunctionalityModels);
		ruleModel.setCreationDate(rule.getCreationDate());
		
		return ruleModel;
	}
}
