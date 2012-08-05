package com.ing3nia.parentalcontrol.services.models.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class RuleModelUtils {
	public static RuleModel convertToRuleModel(Key ruleKey) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCRule rule = pm.getObjectById(PCRule.class, ruleKey);
		RuleModel ruleModel = new RuleModel();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		ruleModel.setKeyId(KeyFactory.keyToString(ruleKey));
		
		ruleModel.setStartDate(formatter.format(rule.getStartDate()));
		ruleModel.setEndDate(formatter.format(rule.getEndDate()));
		ruleModel.setCreationDate(formatter.format(rule.getCreationDate()));		
		
		ArrayList<Integer> disabledFunctionalityModels = new ArrayList<Integer>();
		
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
	
	public static RuleModel convertToRuleModel(PCRule rule) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		RuleModel ruleModel = new RuleModel();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		if (rule.getKey() != null) {
			ruleModel.setKeyId(KeyFactory.keyToString(rule.getKey()));
		}
		
		ruleModel.setStartDate(formatter.format(rule.getStartDate()));
		ruleModel.setEndDate(formatter.format(rule.getEndDate()));
		ruleModel.setCreationDate(formatter.format(rule.getCreationDate()));
		ruleModel.setName(rule.getName());
		
		ArrayList<Integer> disabledFunctionalityModels = new ArrayList<Integer>();
		
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
	
	public static void removeRule(ArrayList<RuleModel> rules, String keyId) {
		int position = 0;
		boolean found = false;
		int size = rules.size();
		
		for (int i = 0; i < size; i++) {
			if (rules.get(i).getKeyId().equals(keyId)) {
				position = i;
				found = true;
				break;
			}
		}
		
		if (found) {
			rules.remove(position);
		}
	}
	
	public static void addRule(PersistenceManager pm, ArrayList<RuleModel> rules, Key keyId) {
		rules.add(convertToRuleModel(keyId));
	}
}
