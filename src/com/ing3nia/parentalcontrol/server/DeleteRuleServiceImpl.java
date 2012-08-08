package com.ing3nia.parentalcontrol.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.client.rpc.DeleteRuleService;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
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

		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		String rulesKey = smartKey + SmartphoneCacheParams.RULES;
		IdentifiableValue ident = (IdentifiableValue) syncCache.getIdentifiable(rulesKey);
		if(ident!=null){
			int pos =0;
			ArrayList<RuleModel> rules = (ArrayList<RuleModel>)ident.getValue();
			for(RuleModel cacheRule : rules){
				if(cacheRule.getKeyId().equals(cacheRule)){
					break;
				}
				pos+=1;
			}
			if(pos<rules.size()){
				rules.remove(pos);
				WriteToCache.writeSmartphoneRulesToCache(rules, smartKey);
			}
		}
		
		
		String modificationCacheKey = smartKey + SmartphoneCacheParams.MODIFICATION;
		IdentifiableValue cacheIdentModification = (IdentifiableValue) syncCache.getIdentifiable(modificationCacheKey);
		if(cacheIdentModification != null){
			ModificationModel modificationModel = (ModificationModel)cacheIdentModification.getValue();
			int pos = 0;
			ArrayList<RuleModel> addedRules = modificationModel.getRules();
			for(RuleModel addRule : addedRules){
				if(addRule.getKeyId().equals(ruleKey)){
					break;
				}
				pos+=1;
			}
			if(pos<addedRules.size()){
				addedRules.remove(pos);
				modificationModel.setRules(addedRules);
			}
			
			pos = 0;
			ArrayList<String> deletedRules = modificationModel.getDeletedRules();
			for(String deletedRuleKey : deletedRules){
				if(!deletedRules.contains(deletedRuleKey)){
					deletedRules.add(deletedRuleKey);
					modificationModel.setDeletedRules(deletedRules);
					break;
				}
			}
			WriteToCache.writeSmartphoneModificationToCache(smartKey, modificationModel);
		}
		
		return result;
	}
}
