package com.ing3nia.parentalcontrol.server;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.rpc.DeleteRuleService;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class DeleteRuleServiceImpl extends RemoteServiceServlet implements DeleteRuleService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DeleteRuleServiceImpl.class.getName());
	
	public DeleteRuleServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean deleteRule(String ruleKey, String smartKey) {
		boolean result = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		Key smartphoneKey = KeyFactory.stringToKey(smartKey);
		PCSmartphone smartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);				
		PCModification modification = pm.getObjectById(PCModification.class, smartphone.getModification());
		
		logger.info("[DeleteRuleService] Converting String to key.");
		Key ruleRealKey = KeyFactory.stringToKey(ruleKey);
			
		logger.info("[DeleteRuleService] Searching for rule with key: " + ruleKey);
		PCRule rule = pm.getObjectById(PCRule.class, ruleRealKey);
			
		try {
			logger.info("[DeleteRuleService] Deleting rule.");
			pm.deletePersistent(rule);
			smartphone.getRules().remove(ruleRealKey);
			
			if(modification.getRules().contains(ruleRealKey)){
				modification.getRules().remove(ruleRealKey);
			}
			
			if(!modification.getDeletedRules().contains(ruleKey)){
				modification.getDeletedRules().add(ruleKey);
			}
			
			result = true;		
		}		
		catch (Exception ex) {
			result = false;
		}
		finally {
			pm.close();
		}		
		
		return result;
	}
}
