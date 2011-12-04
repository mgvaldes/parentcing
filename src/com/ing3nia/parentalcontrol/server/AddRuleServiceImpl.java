package com.ing3nia.parentalcontrol.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.rpc.AddRuleService;
import com.ing3nia.parentalcontrol.client.utils.ModelLogger;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCRule;

import com.ing3nia.parentalcontrol.models.PCSmartphone;

import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AddRuleServiceImpl extends RemoteServiceServlet implements AddRuleService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AddRuleServiceImpl.class.getName());
	
	public AddRuleServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String addRule(String cid, String smartphoneKey, RuleModel newRule) {
		String newRuleKey = null;
//		PCSession session = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCRule rule = new PCRule();
		try {
			//TODO revisar sesion
//			logger.info("[Add Rule Service] Finding user session from cookie");
//			session = SessionUtils.getPCSessionFromCookie(pm, cid);
//			
//			logger.info("[Add Rule Service] Session found. Getting User from session");
//			PCUser user = pm.getObjectById(PCUser.class, session.getUser());
			
//			logger.info("[Add Rule Service] Obtaining smartphone from provided key " + smartphoneKey);
			PCSmartphone smartphone = (PCSmartphone)pm.getObjectById(PCSmartphone.class, KeyFactory.stringToKey(smartphoneKey));
									
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			Date date;
			
			//PCRule rule = new PCRule();
			
			date = sdf.parse(newRule.getCreationDate());
			rule.setCreationDate(date);
			date = sdf.parse(newRule.getStartDate());
			rule.setStartDate(date);
			date = sdf.parse(newRule.getEndDate());
			rule.setEndDate(date);
			rule.setName(newRule.getName());
			rule.setType(newRule.getType());
			
			logger.info("[Add Rule Service] Setting new funcionalities to rules");
			PersistenceManager pm2 = ServiceUtils.PMF.getPersistenceManager();
			rule.setDisabledFunctionalities(getNewFuncionalitiesAsKeys(pm2, newRule));

			pm.makePersistent(rule);
			
			smartphone.getRules().add(rule.getKey());
			
			//logger.severe("[Add Rule Service] Rule saved! "+rule.getName()+" "+rule.getCreationDate()+" "+rule.getStartDate()+" "+rule.getEndDate()+" "+rule.getDisabledFunctionalities().size()+" "+rule.getKey());
			
			
			logger.severe("[Add Rule Service] New rule's key: " + rule.getKey());

		}
		catch (Exception e) {
			logger.severe("[Add Rule Service] Error while saving new rule " + e);
			newRuleKey = null;
		}
		finally {
			
			pm.close();
		}
		
		newRuleKey = KeyFactory.keyToString(rule.getKey());
		logger.severe("RULE DATE AND KEY: "+rule.getCreationDate()+" "+newRuleKey);
		return newRuleKey;
	}

//		logger.info("[Add Rule Service] Finding user session from cookie");
//		
//		try {
//			session = SessionUtils.getPCSessionFromCookie(pm, cid);
//		} 
//		catch (SessionQueryException e) {
//			logger.warning("[Add Rule Service] No session exists for the given cookie. " + e.getMessage());
//			return newRuleKey;
//		}
//
//		// TODO verify if session is active
//
//		logger.info("[Add Rule Service] Session found. Getting User from session");
//		PCUser user = pm.getObjectById(PCUser.class, session.getUser());
//
//		if (user == null) {
//			logger.severe("[Add Rule Service] No user associated with a valid session");
//			return newRuleKey;
//		}
//		
//		// TODO check if smartphone corresponds to USER
//
//		// get smartphone from provided key
//		logger.info("[Add Rule Service] Obtaining smartphone from provided key " + smartphoneKey);
//		PCSmartphone smartphone = (PCSmartphone)pm.getObjectById(PCSmartphone.class, KeyFactory.stringToKey(smartphoneKey));
//		
//		if (smartphone == null) {
//			logger.severe("[Add Rule Service] No smartphone found from the given key " + smartphoneKey);
//			return newRuleKey;
//		}
//		
//		PCRule rule = new PCRule();
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
//		Date date;
//		
//		try {
//			date = sdf.parse(newRule.getCreationDate());
//			rule.setCreationDate(date);
//		} 
//		catch (ParseException e) {
//			logger.severe("[Add Rule Service] Error while parsing rule creating date: " + newRule.getCreationDate());
//			return newRuleKey;
//		}
//		
//		try {
//			date = sdf.parse(newRule.getStartDate());
//			rule.setStartDate(date);
//		} 
//		catch (ParseException e) {
//			logger.severe("[Add Rule Service] Error while parsing rule start date: " + newRule.getStartDate());
//			return newRuleKey;
//		}
//				
//		try {
//			date = sdf.parse(newRule.getEndDate());
//			rule.setEndDate(date);
//		} 
//		catch (ParseException e) {
//			logger.severe("[Add Rule Service] Error while parsing rule end date: " + newRule.getEndDate());
//			return newRuleKey;
//		}
//				
//		rule.setName(newRule.getName());
//		
//		logger.info("[Add Rule Service] Setting new funcionalities to rules");
//		ArrayList<Key> newDisabledFuncionalities = getNewFuncionalitiesAsKeys(pm, newRule);
//		
//		if (newDisabledFuncionalities != null) {
//			rule.setDisabledFunctionalities(newDisabledFuncionalities);
//		}
//		else {
//			return newRuleKey;
//		}
//		
//		try {
//			pm.makePersistent(rule);
//			logger.severe("[Add Rule Service] Rule saved!");
//			newRuleKey = KeyFactory.keyToString(rule.getKey());
//			logger.severe("[Add Rule Service] New rule's key: " + newRuleKey);
//		}
//		catch (Exception e) {
//			logger.severe("[Add Rule Service] Error while saving new rule " + e.getMessage());
//			return newRuleKey;
//		}
//		finally {
//			pm.close();
//		}
//		
//		return newRuleKey;
	//}
	
	private static ArrayList<Key> getNewFuncionalitiesAsKeys(PersistenceManager pm, RuleModel ruleModel) {
		Logger logger = ModelLogger.logger;

		Query query = pm.newQuery(PCFunctionality.class);
		query.setFilter("id == id_param");
		query.declareParameters("int id_param");
		query.setRange(0, 1);

		int idFuncionality;
		ArrayList<Key> newDisabledFuncionalities = new ArrayList<Key>();
		
		for (Integer idFunc : ruleModel.getDisabledFunctionalities()) {
			PCFunctionality funcionality;

			idFuncionality = idFunc;
			logger.info("[Add Rule Service] Finding funcionality by id: " + idFuncionality);
			
			try {
				List<PCFunctionality> results = (List<PCFunctionality>) query.execute(idFuncionality);
				
				if (!results.isEmpty()) {
					logger.info("[Add Rule Service] Returning found PCFuncionality");
					funcionality = results.get(0);
					newDisabledFuncionalities.add(funcionality.getKey());
				} 
				else {
					logger.severe("[Add Rule Service] No funcionality with id: " + idFuncionality + " was found ");
					newDisabledFuncionalities = null;
				}
			} 
			catch (Exception e) {
				logger.info("[Add Rule Service] Could not find funcionality from id: " + idFuncionality + " " + e.getMessage());
				newDisabledFuncionalities = null;
			}
		}
		pm.close();
		
		return newDisabledFuncionalities;
	}
/*
	@Override
	public String addRule(String cid, String smartphoneKey, RuleModel newRule)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub

		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		//createDummyApplication();
		
		try {
			PCRule pcrule = new PCRule();
			Date date = new Date();
			pcrule.setStartDate(date);
			pcrule.setCreationDate(date);
			pcrule.setEndDate(date);
			pcrule.setName("RULE NAME");

			pm.makePersistent(pcrule);
			logger.severe("RULE KEY: "+pcrule.getKey());
			return "null";
		} catch (Exception e) {
			return null;
		}finally{
			pm.close();
		}
	}*/
}
