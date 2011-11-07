package com.ing3nia.parentalcontrol.services.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.PersistenceManager;

import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class RuleModel {
	private String keyId;
	
	private String startDate;
	
	private String endDate;
	
	private ArrayList<Integer> disabledFunctionalities;
	
	private String creationDate;

	public RuleModel() {
		super();
	}

	public RuleModel(String keyId, String startDate, String endDate,
			ArrayList<Integer> disabledFunctionalities) {
		super();
		this.keyId = keyId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.disabledFunctionalities = disabledFunctionalities;
	}
	
	public RuleModel(String startDate, String endDate,
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

	public static RuleModel convertToRuleModel(PCRule rule) {
		RuleModel ruleModel = new RuleModel();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		
		ruleModel.setStartDate(formatter.format(rule.getStartDate()));
		ruleModel.setEndDate(formatter.format(rule.getEndDate()));
		
		ArrayList<Integer> disabledFunctionalityModels = new ArrayList<Integer>();
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		ArrayList<PCFunctionality> disabledFuncionalities = (ArrayList<PCFunctionality>)pm.getObjectById(rule.getDisabledFunctionalities());
		for (PCFunctionality functionality : disabledFuncionalities) {
			disabledFunctionalityModels.add(functionality.getId());
		}
		
		ruleModel.setDisabledFunctionalities(disabledFunctionalityModels);
		
		return ruleModel;
	}
}
