package com.ing3nia.parentalcontrol.client.models.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class RuleModelUtils {
	public static RuleModel convertToRuleModel(PCRule rule) {
		RuleModel ruleModel = new RuleModel();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		
		ruleModel.setStartDate(formatter.format(rule.getStartDate()));
		ruleModel.setEndDate(formatter.format(rule.getEndDate()));
		ruleModel.setCreationDate(formatter.format(rule.getCreationDate()));		
		
		ArrayList<Integer> disabledFunctionalityModels = new ArrayList<Integer>();
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		ArrayList<Key> funcKeys = rule.getDisabledFunctionalities();
		PCFunctionality disabledFuncionality; 
		
		for (Key functionality : funcKeys) {
			disabledFuncionality = (PCFunctionality)pm.getObjectById(PCFunctionality.class, functionality);
			disabledFunctionalityModels.add(disabledFuncionality.getId());
		}
		
		ruleModel.setDisabledFunctionalities(disabledFunctionalityModels);
		
		pm.close();
		
		return ruleModel;
	}
}
